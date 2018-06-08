package cn.jesse.gaea.plugin.main

import android.os.Bundle
import cn.jesse.gaea.lib.base.ui.BaseActivity

class MainActivity : BaseActivity() {

    override fun getTag(): String {
        return "Main.MainActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.main_activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}