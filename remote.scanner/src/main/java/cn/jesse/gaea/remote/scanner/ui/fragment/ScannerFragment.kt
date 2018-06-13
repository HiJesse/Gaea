package cn.jesse.gaea.remote.scanner.ui.fragment

import android.os.Bundle
import android.taobao.atlas.remote.IRemote
import android.taobao.atlas.remote.IRemoteTransactor
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.remote.scanner.R
import cn.jesse.gaea.remote.scanner.constant.PluginDef
import kotlinx.android.synthetic.main.scanner_fragment_scan.*

/**
 * 二维码条形码扫描 fragment
 *
 * @author Jesse
 */
class ScannerFragment : BaseFragment(), IRemote {
    private var surfaceHolder: SurfaceHolder? = null

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.ScannerFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scanner_fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        surfaceHolder = svScanner.holder
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun call(commandName: String?, args: Bundle?, callback: IRemoteTransactor.IResponse?): Bundle {
        return null as Bundle
    }

    override fun <T : Any?> getRemoteInterface(interfaceClass: Class<T>?, args: Bundle?): T? {
        return null
    }

    companion object {
        fun newInstance(): BaseFragment {
            return ScannerFragment()
        }
    }
}