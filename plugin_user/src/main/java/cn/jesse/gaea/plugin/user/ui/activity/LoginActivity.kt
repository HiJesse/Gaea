package cn.jesse.gaea.plugin.user.ui.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import cn.jesse.gaea.lib.base.livedata.DataStatusResult
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.plugin.user.R
import cn.jesse.gaea.plugin.user.bean.LoginBean
import cn.jesse.gaea.plugin.user.constant.PluginDef
import cn.jesse.gaea.plugin.user.vm.LoginViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.user_activity_login.*

/**
 * 登录界面
 *
 * @author Jesse
 */
class LoginActivity : BaseActivity() {
    private lateinit var loginViewModel: LoginViewModel

    private val loginResultObserver = Observer<DataStatusResult<LoginBean>> {result ->
        if (result!!.succeed) {
            Toasty.normal(this, "用户 ${result.data?.nickname} 登录成功").show()
        } else {
            Toasty.normal(this, "登录失败 ${result.message}").show()
        }

        setLoginStatus(result.succeed)
    }

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.LoginActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.user_activity_login
    }

    override fun onActivityCreated() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(this, loginResultObserver)

        btnLoginSucceed.setOnClickListener {
            loginViewModel.login()
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
        bundle.putBoolean(RemoteRouterDef.PluginUser.RESULT_LOGIN_STATUS, loginStatus)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}