package cn.jesse.gaea.lib.base.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import cn.jesse.nativelogger.NLogger
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL

/**
 * 网络相关工具
 *
 * @author Jesse
 */
object NetworkUtil {
    val TAG = "NetworkUtil"
    var NET_CNNT_BAIDU_OK = 1 // NetworkAvailable
    var NET_CNNT_BAIDU_TIMEOUT = 2 // no NetworkAvailable
    var NET_NOT_PREPARE = 3 // Net no ready
    var NET_ERROR = 4 //net error
    private val TIMEOUT = 3000 // TIMEOUT

    /**
     * 获取本地ip地址
     */
    fun getIpAddress(): String {
        var ret = ""
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val element = en.nextElement()
                val enumIpAddress = element.inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val ip = enumIpAddress.nextElement()
                    if (!ip.isLoopbackAddress) {
                        ret = ip.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            NLogger.e(TAG, "getIpAddress ${ex.message}")
            ret = ""
        }

        return ret
    }


    /**
     * 当前网络是否正常
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return CheckUtil.isNotNull(info) && info.isAvailable
    }

    /**
     * 返回当前网络状态
     */
    fun getNetState(context: Context): Int {
        try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivity.activeNetworkInfo
            return if (networkInfo.isAvailable && networkInfo.isConnected) {
                if (!connectionNetwork())
                    NET_CNNT_BAIDU_TIMEOUT
                else
                    NET_CNNT_BAIDU_OK
            } else {
                NET_NOT_PREPARE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return NET_ERROR
    }

    /**
     * ping "http://www.baidu.com" 校验网络是否连接正常
     */
    private fun connectionNetwork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL("http://www.baidu.com")
                    .openConnection() as HttpURLConnection
            httpUrl.connectTimeout = TIMEOUT
            httpUrl.connect()
            result = true
        } catch (e: IOException) {
            NLogger.e(TAG, "connectionNetwork ${e.message}")
        } finally {
            if (null != httpUrl) {
                httpUrl.disconnect()
            }
        }
        return result
    }

    /**
     * check is3G
     * @param context
     * @return boolean
     */
    fun is3G(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return CheckUtil.isNotNull(activeNetInfo) && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * isWifi
     */
    fun isWifi(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return CheckUtil.isNotNull(activeNetInfo) && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * is2G
     */
    fun is2G(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return CheckUtil.isNotNull(activeNetInfo) && (
                        activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE ||
                        activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS ||
                        activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_CDMA
                )
    }

    /**
     * wifi是否打开
     */
    fun isWifiEnabled(context: Context): Boolean {
        val mgrConn = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mgrTel = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mgrConn.activeNetworkInfo != null && mgrConn
                .activeNetworkInfo.state == NetworkInfo.State.CONNECTED || mgrTel
                .networkType == TelephonyManager.NETWORK_TYPE_UMTS
    }

}