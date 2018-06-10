package cn.jesse.gaea.plugin.user.ui.activity

import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.plugin.user.R

class LoginActivity : BaseActivity() {

    override fun getLogTag(): String {
        return "User.LoginActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.user_activity_login
    }
}