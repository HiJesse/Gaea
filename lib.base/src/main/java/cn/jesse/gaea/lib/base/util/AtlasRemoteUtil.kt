package cn.jesse.gaea.lib.base.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.taobao.atlas.remote.IRemote
import android.taobao.atlas.remote.IRemoteTransactor
import android.taobao.atlas.remote.RemoteFactory
import android.taobao.atlas.remote.RemoteFactory.requestRemote
import android.taobao.atlas.remote.fragment.RemoteFragment
import android.taobao.atlas.remote.transactor.RemoteTransactor
import cn.jesse.nativelogger.NLogger
import es.dmoral.toasty.Toasty

/**
 * atlas remote 相关工具
 * remote view
 * remote fragment
 * remote transactor
 *
 * @author Jesse
 */
object AtlasRemoteUtil {
    private val TAG = "AtlasRemoteUtil"

    /**
     * 获取remote fragment
     */
    fun fetchRemoteFragment(activity: Activity, action: String, listener: RemoteFactory.OnRemoteStateListener<RemoteFragment>) {
        val intent = Intent(action)
        requestRemote(RemoteFragment::class.java, activity, intent, listener)
    }

    /**
     * 获取remote transactor
     *
     * @param activity context
     * @param action 标记
     * @param clazz T
     * @param succeedListener 成功监听
     * @param failedListener 失败监听
     */
    fun <T> fetchRemoteTransactor(activity: Activity, action: String, clazz: Class<T>,
                                  succeedListener: ((T) -> Unit),
                                  failedListener: ((msg: String?) -> Unit)? = null) {
        val intent = Intent(action)
        requestRemote(RemoteTransactor::class.java, activity, intent, object : RemoteFactory.OnRemoteStateListener<RemoteTransactor> {
            override fun onFailed(errorInfo: String?) {
                NLogger.e(TAG, "fetchRemoteTransactor onFailed $errorInfo")
                Toasty.normal(activity, "调起失败 $errorInfo").show()
                if (CheckUtil.isNotNull(failedListener)) {
                    failedListener!!.invoke(errorInfo)
                }
            }

            override fun onRemotePrepared(remote: RemoteTransactor?) {
                if (CheckUtil.isNull(remote)) {
                    failedListener?.invoke("调起准备失败")
                    return
                }

                registerHostTransactor(remote!!)
                succeedListener.invoke(remote.getRemoteInterface<T>(clazz, null))
            }

        })
    }

    /**
     * 注册 host transactor
     */
    private fun registerHostTransactor(remote: RemoteTransactor) {
        remote.registerHostTransactor(object : IRemote {
            override fun call(s: String, bundle: Bundle, iResponse: IRemoteTransactor.IResponse): Bundle? {
                NLogger.i(TAG, "registerHostTransactor call $s")
                return null
            }

            override fun <T> getRemoteInterface(aClass: Class<T>, bundle: Bundle): T? {
                return null
            }
        })
    }
}