package cn.jesse.gaea.plugin.user.ui.activity

import android.app.Activity
import android.os.Bundle
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.plugin.user.R
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