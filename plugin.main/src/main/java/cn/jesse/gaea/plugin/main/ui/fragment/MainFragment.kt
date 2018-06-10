package cn.jesse.gaea.plugin.main.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.plugin.main.R
import kotlinx.android.synthetic.main.main_fragment_main.*

/**
 * 首页Fragment
 *
 * @author Jesse
 */
class MainFragment : BaseFragment() {

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
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): BaseFragment {
            return MainFragment()
        }
    }
}