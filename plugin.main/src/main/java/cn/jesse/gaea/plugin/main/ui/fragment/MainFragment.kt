package cn.jesse.gaea.plugin.main.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.plugin.main.R
import cn.jesse.nativelogger.NLogger
import kotlinx.android.synthetic.main.main_fragment_main.*

/**
 * 首页Fragment
 *
 * @author Jesse
 */
class MainFragment : BaseFragment() {
    private var loginStatus = false

    override fun getLogTag(): String {
        return "Main.MainFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            val intent = Intent()
            intent.setClassName(activity, "cn.jesse.gaea.plugin.user.ui.activity.LoginActivity")
            startActivityForResult(intent, 1000)
        }

        showLoginStatus()
    }

    /**
     * 显示用户登录状态
     */
    private fun showLoginStatus() {
        if (loginStatus) {
            tvLoginStatus.text = "(已登录)"
            tvLoginStatus.setTextColor(Color.GREEN)
        } else {
            tvLoginStatus.text = "(未登录)"
            tvLoginStatus.setTextColor(Color.RED)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            loginStatus = data?.extras?.getBoolean("STATUS_LOGIN")!!
            NLogger.d(mTag, "login status $loginStatus")
            showLoginStatus()
        }
    }

    companion object {
        fun newInstance(): BaseFragment {
            return MainFragment()
        }
    }
}