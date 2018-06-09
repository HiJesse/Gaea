package cn.jesse.gaea.plugin.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.plugin.main.R

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

    companion object {
        fun newInstance(): BaseFragment {
            return MainFragment()
        }
    }
}