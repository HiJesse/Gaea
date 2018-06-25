package cn.jesse.gaea.lib.common.vm

import android.arch.lifecycle.MutableLiveData
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.api.UpdateService
import cn.jesse.gaea.lib.common.bean.CheckBundleUpdateBean
import cn.jesse.gaea.lib.common.dataset.DataSetManager
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
    val updateBundleResult: MutableLiveData<DataStatusResult<CheckBundleUpdateBean>> = MutableLiveData()

    /**
     * 检查bundle更新
     */
    fun checkBundleUpdate() {

        HttpEngine.getInstance()
                .create(UpdateService::class.java)
                .checkBundle()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({data ->
                    DataSetManager.getAppDataSet().hostVersion = data.hostVersion
                    DataSetManager.getAppDataSet().bundlesInfo = data.bundles
                    updateBundleResult.value = DataStatusResult(data)

                }, {e ->
                    NLogger.e(TAG, "checkBundleUpdate ${e.message}")
                    updateBundleResult.value = DataStatusResult(false, "${e.message}")
                })
    }
}