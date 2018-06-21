package cn.jesse.gaea.plugin.user.ui.activity

import android.app.Activity
import android.os.Bundle
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.ui.BaseActivity
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.gaea.lib.network.transformer.ResponseTransformer
import cn.jesse.gaea.plugin.user.R
import cn.jesse.gaea.plugin.user.api.UserService
import cn.jesse.nativelogger.NLogger
import kotlinx.android.synthetic.main.user_activity_login.*

class LoginActivity : BaseActivity() {

    override fun getLogTag(): String {
        return "User.LoginActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.user_activity_login
    }

    override fun onActivityCreated() {
        HttpEngine.getInstance()
                .create(UserService::class.java)
                .login()
                .compose(IOMainThreadTransformer())
                .compose(ResponseTransformer())
                .subscribe({data ->
                    NLogger.d(mTag, "${data.nickname}")
                }, {e ->
                    NLogger.e(mTag, "error $e")
                })

        btnLoginSucceed.setOnClickListener {
            setLoginStatus(true)
        }

        btnLoginFailed.setOnClickListener {
            setLoginStatus(false)
        }
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