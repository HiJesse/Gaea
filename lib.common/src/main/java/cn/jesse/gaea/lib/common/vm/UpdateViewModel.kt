package cn.jesse.gaea.lib.common.vm

import android.arch.lifecycle.MutableLiveData
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.FileDownloaderUtil
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.api.UpdateService
import cn.jesse.gaea.lib.common.bean.CheckUpdateBean
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.gaea.lib.common.util.WorkspaceUtil
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer
import cn.jesse.nativelogger.NLogger

/**
 * 更新相关 view model
 *
 * @author Jesse
 */
class UpdateViewModel : BaseViewModel() {
    private val TAG = "UpdateViewModel"
    val updateResult: MutableLiveData<DataStatusResult<CheckUpdateBean>> = MutableLiveData()

    /**
     * 检查bundle更新
     */
    fun checkBundleUpdate() {

        HttpEngine.getInstance()
                .create(UpdateService::class.java)
                .checkUpdate()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({data ->
                    DataSetManager.getAppDataSet().hostVersion = data.hostVersion
                    extractPatch(data)
                    DataSetManager.getAppDataSet().bundlesInfo = data.bundles
                    updateResult.value = DataStatusResult(data)

                }, {e ->
                    NLogger.e(TAG, "checkBundleUpdate ${e.message}")
                    updateResult.value = DataStatusResult(false, "${e.message}")
                })
    }

    /**
     * 解析t patch更新部分
     */
    private fun extractPatch(updateInfo: CheckUpdateBean) {
        if (CheckUtil.isNull(updateInfo.tPatch)) {
            return
        }

        DataSetManager.getAppDataSet().patchInfo = updateInfo.tPatch
        val urls = arrayOf(updateInfo.tPatch!!.patchConfigDownloadUrl, updateInfo.tPatch!!.patchDownloadUrl)

        FileDownloaderUtil.download(urls, WorkspaceUtil.getInstance().patchFiles.absolutePath, {
            NLogger.i(TAG, "extractPatch download succeed")
        }, {
            NLogger.e(TAG, "extractPatch download failed")
            WorkspaceUtil.getInstance().clear(WorkspaceUtil.getInstance().patchFiles)
        })
    }
}