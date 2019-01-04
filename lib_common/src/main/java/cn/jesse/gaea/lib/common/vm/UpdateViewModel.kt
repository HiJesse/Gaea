package cn.jesse.gaea.lib.common.vm

import android.arch.lifecycle.MutableLiveData
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.base.util.FileDownloaderUtil
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.api.UpdateService
import cn.jesse.gaea.lib.common.bean.CheckUpdateBean
import cn.jesse.gaea.lib.common.constant.PluginDef
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.gaea.lib.common.util.AtlasUpdateUtil
import cn.jesse.gaea.lib.common.util.WorkspaceUtil
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer
import cn.jesse.nativelogger.NLogger
import es.dmoral.toasty.Toasty

/**
 * 更新相关 view model
 *
 * @author Jesse
 */
class UpdateViewModel : BaseViewModel() {
    private val TAG = "${PluginDef.TAG}.UpdateViewModel"
    val updateResult: MutableLiveData<DataStatusResult<CheckUpdateBean>> = MutableLiveData()

    /**
     * 检查更新
     *
     * @param mode 模式
     */
    fun checkUpdate(mode: Mode) {

        val updateRequest = HttpEngine.getInstance()
                .create(UpdateService::class.java)
                .checkUpdate()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({ data ->
                    DataSetManager.getAppDataSet().hostVersion = data.hostVersion

                    when (mode) {
                        Mode.PATCH -> extractPatch(data)
                        Mode.BUNDLE -> DataSetManager.getAppDataSet().bundlesInfo = data.bundles
                        Mode.PATCH_BUNDLE -> {
                            extractPatch(data)
                            DataSetManager.getAppDataSet().bundlesInfo = data.bundles
                        }
                        else -> {
                            // unused
                        }
                    }

                    updateResult.value = DataStatusResult(data)

                }, { e ->
                    NLogger.e(TAG, "checkUpdate ${e.message}")
                    updateResult.value = DataStatusResult(false, "${e.message}")
                })
        addCancelableRequest(updateRequest)
    }

    /**
     * 解析t patch更新部分
     */
    private fun extractPatch(updateInfo: CheckUpdateBean) {
        if (CheckUtil.isNull(updateInfo.tPatch)) {
            return
        }

        DataSetManager.getAppDataSet().patchInfo = updateInfo.tPatch
        val urls = arrayOf(updateInfo.tPatch!!.patchConfigDownloadUrl!!, updateInfo.tPatch!!.patchDownloadUrl!!)

        var jsonFile: String? = null
        var patchFile = jsonFile

        FileDownloaderUtil.download(urls, WorkspaceUtil.getInstance().patchFiles.absolutePath, { task ->
            NLogger.i(TAG, "extractPatch download succeed ${task?.url} ${task?.targetFilePath}")
            if (CheckUtil.isNull(task)) {
                return@download
            }

            if (task!!.targetFilePath.endsWith(".json")) {
                jsonFile = task.targetFilePath
            } else if (task.targetFilePath.endsWith(".tpatch")) {
                patchFile = task.targetFilePath
            }

            if (CheckUtil.isNull(jsonFile) || CheckUtil.isNull(patchFile)) {
                NLogger.i(TAG, "extractPatch wait for .json | .tpatch")
                return@download
            }

            NLogger.i(TAG, "extractPatch json & patch download Succeed")

            AtlasUpdateUtil.loadTPatch(jsonFile!!, patchFile!!, updateInfo.tPatch!!.patchFileMD5!!, { succeed ->
                var msg = "加载失败, 请查看日志"

                if (succeed) {
                    msg = "加载成功, 重启生效"
                    DataSetManager.getAppDataSet().patchInfo?.patchInstalled = true
                }

                Toasty.normal(ContextUtil.getApplicationContext(), msg).show()
            })

        }, {
            NLogger.e(TAG, "extractPatch download failed")
            WorkspaceUtil.getInstance().clear(WorkspaceUtil.getInstance().patchFiles)
        })
    }

    /**
     * 检查升级模式
     */
    enum class Mode {
        PATCH,
        BUNDLE,
        PATCH_BUNDLE,
        APP
    }
}