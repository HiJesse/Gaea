package cn.jesse.gaea.plugin.user.vm

import android.arch.lifecycle.MutableLiveData
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer
import cn.jesse.gaea.plugin.user.api.UserService
import cn.jesse.gaea.plugin.user.bean.LoginBean

/**
 * 登录 ViewModel
 *
 * @author Jesse
 */
class LoginViewModel : BaseViewModel() {
    val loginResult: MutableLiveData<DataStatusResult<LoginBean>> = MutableLiveData()

    fun login() {

        HttpEngine.getInstance()
                .create(UserService::class.java)
                .login()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({data ->
                    DataSetManager.getUserDataSet().accessToken = data.accessToken
                    DataSetManager.getUserDataSet().nickname = data.nickname
                    loginResult.value = DataStatusResult(data)
                }, {e ->
                    loginResult.value = DataStatusResult(false, "${e.message}")
                })
    }
}