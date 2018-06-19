package cn.jesse.gaea.plugin.main.ui.activity

import android.support.v4.view.ViewPager
import android.taobao.atlas.remote.RemoteFactory
import android.taobao.atlas.remote.fragment.RemoteFragment
import android.view.MenuItem
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.AppUtil
import cn.jesse.gaea.lib.base.util.AtlasRemoteUtil
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.DoubleExitUtil
import cn.jesse.gaea.lib.base.constant.RemoteRouterDef
import cn.jesse.gaea.plugin.main.R
import cn.jesse.gaea.plugin.main.ui.adapter.ViewPagerAdapter
import cn.jesse.gaea.plugin.main.constant.PluginDef
import cn.jesse.nativelogger.NLogger
import kotlinx.android.synthetic.main.main_activity_main.*

/**
 * App 首页页面
 *
 * @author Jesse
 */
class MainActivity : BaseActivity(), RemoteFactory.OnRemoteStateListener<RemoteFragment> {

    private var menuItem: MenuItem? = null
    private var adapter = ViewPagerAdapter(supportFragmentManager)

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.MainActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.main_activity_main
    }

    override fun onActivityCreated() {
        // 设置退出app 监听
        DoubleExitUtil.getInstance().listener = {
            NLogger.d(mTag, "exit app")
            finish()
            AppUtil.exitProcess()
        }

        // 设置BottomNavigationView切换监听
        bnView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_navigation_home -> {
                    vpContent.currentItem = 0
                    false
                }
                R.id.main_navigation_discovery -> {
                    vpContent.currentItem = 1
                    false
                }
                R.id.main_navigation_mine -> {
                    vpContent.currentItem = 2
                    false
                }
                else -> {
                    vpContent.currentItem = 0
                    false
                }
            }
        }

        // 设置ViewPager切换监听
        vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // unused
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // unused
            }

            override fun onPageSelected(position: Int) {
                if (CheckUtil.isNull(menuItem)) {
                    bnView.menu.getItem(0).isChecked = false
                } else {
                    menuItem!!.isChecked = false
                }

                menuItem = bnView.menu.getItem(position)
                menuItem!!.isChecked = true
            }

        })

        vpContent.adapter = adapter
        AtlasRemoteUtil.fetchRemote(this, RemoteRouterDef.PluginUser.FRAGMENT_USER_CENTER, RemoteFragment::class.java, this)
    }

    override fun onFailed(errorInfo: String?) {
        NLogger.e(mTag, "atlas remote onFailed $errorInfo")
    }

    override fun onRemotePrepared(remote: RemoteFragment?) {
        NLogger.e(mTag, "onRemotePrepared")
        adapter.addFragment(remote!!)
    }

    override fun onBackPressed() {
        // 两次点击退出app
        DoubleExitUtil.getInstance().click()
    }
}