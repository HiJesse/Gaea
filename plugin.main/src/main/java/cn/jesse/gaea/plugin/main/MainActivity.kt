package cn.jesse.gaea.plugin.main

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MenuItem
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.AppUtil
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.DoubleExitUtil
import cn.jesse.gaea.plugin.main.ui.adapter.ViewPagerAdapter
import cn.jesse.nativelogger.NLogger
import kotlinx.android.synthetic.main.main_activity_main.*

/**
 * App 首页页面
 *
 * @author Jesse
 */
class MainActivity : BaseActivity() {
    private var menuItem: MenuItem? = null

    override fun getTag(): String {
        return "Main.MainActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.main_activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置退出app 监听
        DoubleExitUtil.getInstance().listener = {
            NLogger.d(mTag, "exit app")
            finish()
            AppUtil.exitProcess()
        }

        // 设置BottomNavigationView切换监听
        bnView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.main_navigation_home -> vpContent.currentItem = 0
                R.id.main_navigation_discovery -> vpContent.currentItem = 1
                R.id.main_navigation_mine -> vpContent.currentItem = 2
                else -> vpContent.currentItem = 0
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

        vpContent.adapter = ViewPagerAdapter(supportFragmentManager)
    }

    override fun onBackPressed() {
        // 两次点击退出app
        DoubleExitUtil.getInstance().click()
    }
}