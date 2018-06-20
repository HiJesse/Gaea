package cn.jesse.gaea.remote.scanner.ui.activity

import cn.jesse.gaea.lib.common.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.PermissionUtil
import cn.jesse.gaea.remote.scanner.R
import cn.jesse.gaea.remote.scanner.constant.PluginDef
import cn.jesse.gaea.remote.scanner.ui.fragment.ScannerFragment
import es.dmoral.toasty.Toasty

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
        permissionRequester = PermissionUtil
                .with(this)
                .request(android.Manifest.permission.CAMERA)
                .onAllGranted {
                    initScanner()
                }.onAnyDenied {
                    Toasty.error(this, "摄像头权限被拒").show()
                }.ask(100)
    }

    private fun initScanner() {
        supportFragmentManager.beginTransaction().replace(R.id.flContent, ScannerFragment.newInstance()).commitAllowingStateLoss()
    }

}