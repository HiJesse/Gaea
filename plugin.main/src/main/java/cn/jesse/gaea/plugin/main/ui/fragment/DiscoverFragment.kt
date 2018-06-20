package cn.jesse.gaea.plugin.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.common.ui.BaseFragment
import cn.jesse.gaea.plugin.main.R
import cn.jesse.gaea.plugin.main.constant.PluginDef
import kotlinx.android.synthetic.main.main_fragment_discover.*

/**
 * 发现Fragment
 *
 * @author Jesse
 */
class DiscoverFragment : BaseFragment() {

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.MainFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnScan.setOnClickListener {
            ActivityRouter.startActivity(this, RemoteRouterDef.PluginScanner.ACTIVITY_SCAN)
        }
    }

    companion object {
        fun newInstance(): BaseFragment {
            return DiscoverFragment()
        }
    }
}