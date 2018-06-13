package cn.jesse.gaea.remote.scanner.ui.activity

import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.remote.scanner.R
import cn.jesse.gaea.remote.scanner.constant.PluginDef

/**
 * 二维码条形码扫描 activity
 *
 * @author Jesse
 */
class ScannerActivity : BaseActivity() {

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.ScannerActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.scanner_activity_scan
    }

    override fun onActivityCreated() {

    }

}