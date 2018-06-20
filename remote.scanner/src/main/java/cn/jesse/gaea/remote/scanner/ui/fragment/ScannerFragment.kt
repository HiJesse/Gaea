package cn.jesse.gaea.remote.scanner.ui.fragment

import android.os.Bundle
import android.taobao.atlas.remote.IRemote
import android.taobao.atlas.remote.IRemoteTransactor
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.common.ui.BaseFragment
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.remote.scanner.R
import cn.jesse.gaea.remote.scanner.constant.PluginDef
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.scanner_fragment_scan.*
import java.util.Arrays.asList

/**
 * 二维码条形码扫描 fragment
 *
 * @author Jesse
 */
class ScannerFragment : BaseFragment(), IRemote, BarcodeCallback {
    private lateinit var beepManager: BeepManager
    private var lastText: String? = null

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.ScannerFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scanner_fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beepManager = BeepManager(activity)
        val formats = asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        dbvScanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        dbvScanner.decodeContinuous(this)
    }

    override fun barcodeResult(result: BarcodeResult?) {
        if (CheckUtil.isNull(result) || TextUtils.isEmpty(result!!.text) || result.text == lastText) {
            return
        }

        lastText = result.text
        dbvScanner.setStatusText(lastText)
        beepManager.playBeepSoundAndVibrate()
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        // unused
    }

    override fun onResume() {
        super.onResume()
        dbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        dbvScanner.pause()
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