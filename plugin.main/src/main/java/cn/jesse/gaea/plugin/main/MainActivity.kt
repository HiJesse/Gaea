package cn.jesse.gaea.plugin.main

import android.os.Bundle
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.AppUtil
import cn.jesse.gaea.lib.base.util.DoubleExitUtil
import cn.jesse.nativelogger.NLogger

/**
 * App 首页页面
 *
 * @author Jesse
 */
class MainActivity : BaseActivity() {

    override fun getTag(): String {
        return "Main.MainActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.main_activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DoubleExitUtil.getInstance().listener = {
            NLogger.d(mTag, "exit app")
            finish()
            AppUtil.exitProcess()
        }
    }

    override fun onBackPressed() {
        // 两次点击退出app
        DoubleExitUtil.getInstance().click()
    }
}