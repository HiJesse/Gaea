package cn.jesse.gaea.lib.common.vm

import android.arch.lifecycle.MutableLiveData
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.api.UpdateService
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer

/**
 * 更新相关 view model
 *
 * @author Jesse
 */
class UpdateViewModel : BaseViewModel() {

    val updateBundleResult: MutableLiveData<DataStatusResult<UpdateService>> = MutableLiveData()

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
                }, {e ->
                    updateBundleResult.value = DataStatusResult(false, "${e.message}")
                })
    }
}