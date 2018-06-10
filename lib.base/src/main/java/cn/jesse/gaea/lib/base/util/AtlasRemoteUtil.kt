package cn.jesse.gaea.lib.base.util

import android.app.Activity
import android.content.Intent
import android.taobao.atlas.remote.RemoteFactory
import android.taobao.atlas.remote.RemoteFactory.requestRemote
import android.taobao.atlas.remote.fragment.RemoteFragment

/**
 * atlas remote 相关工具
 * remote view
 * remote fragment
 * remote transactor
 *
 * @author Jesse
 */
object AtlasRemoteUtil {

    fun fetchRemote(activity: Activity, action: String, clazz: Any, listener: RemoteFactory.OnRemoteStateListener<*>) {
        val intent = Intent(action)
        requestRemote(RemoteFragment::class.java, activity, intent, listener)
    }
}