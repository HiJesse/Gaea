package cn.jesse.gaea.plugin.user.ui.activity

import android.app.Activity
import android.os.Bundle
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.gaea.lib.common.ui.BaseActivity
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer
import cn.jesse.gaea.plugin.user.R
import cn.jesse.gaea.plugin.user.api.UserService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.user_activity_login.*

class LoginActivity : BaseActivity() {

    override fun getLogTag(): String {
        return "User.LoginActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.user_activity_login
    }

    override fun onActivityCreated() {
        btnLoginSucceed.setOnClickListener {
            login()
        }

        btnLoginFailed.setOnClickListener {
            setLoginStatus(false)
        }
    }

    /**
     * 登录
     */
    private fun login() {
        HttpEngine.getInstance()
                .create(UserService::class.java)
                .login()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({data ->
                    Toasty.normal(this, "用户 ${data.nickname} 登录成功").show()
                    DataSetManager.getUserDataSet().accessToken = data.accessToken
                    DataSetManager.getUserDataSet().nickname = data.nickname
                    setLoginStatus(true)
                }, {e ->
                    Toasty.normal(this, "${e.message} 登录失败").show()
                    setLoginStatus(false)
                })
    }

    /**
     * 设置是否登录成功, 并关闭页面
     */
    private fun setLoginStatus(loginStatus: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(RemoteRouterDef.PluginUser.PARAMS_LOGIN_STATUS, loginStatus)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}