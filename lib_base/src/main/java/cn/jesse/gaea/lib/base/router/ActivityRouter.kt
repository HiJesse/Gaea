package cn.jesse.gaea.lib.base.router

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import cn.jesse.gaea.lib.base.util.CheckUtil

/**
 * activity 路由
 *
 * @author Jesse
 */
object ActivityRouter {
    val CODE_IGNORE = -1

    /**
     * activity 发起 只跳转
     */
    fun startActivity(activity: Activity, uri: String) {
        startActivity(activity, uri, null, CODE_IGNORE, CODE_IGNORE)
    }

    /**
     * activity 发起 带参数跳转
     */
    fun startActivity(activity: Activity, uri: String, bundle: Bundle?) {
        startActivity(activity, uri, bundle, CODE_IGNORE, CODE_IGNORE)
    }

    /**
     * activity 带activity result跳转
     */
    fun startActivity(activity: Activity, uri: String, requestCode: Int) {
        startActivity(activity, uri, null, CODE_IGNORE, requestCode)
    }

    /**
     * fragment 发起只跳转
     */
    fun startActivity(fragment: Fragment, uri: String) {
        startActivity(fragment, uri, null, CODE_IGNORE, CODE_IGNORE)
    }

    /**
     * fragment 带activity result跳转
     */
    fun startActivity(fragment: Fragment, uri: String, requestCode: Int) {
        startActivity(fragment, uri, null, CODE_IGNORE, requestCode)
    }

    /**
     * activity 发起 带参数跳转
     */
    fun startActivity(fragment: Fragment, uri: String, bundle: Bundle?) {
        startActivity(fragment, uri, bundle, CODE_IGNORE, CODE_IGNORE)
    }

    /**
     * 在activity中打开 activity, 包括跨bundle
     *
     * @param activity activity
     * @param uri 正常情况下的class name.
     * @param bundle 业务数据
     * @param flags 跳转标记
     * @param requestCode 请求code
     */
    fun startActivity(activity: Activity, uri: String, bundle: Bundle?, flags: Int, requestCode: Int) {
        val intent = Intent()
        intent.setClassName(activity, uri)
        if (CheckUtil.isNotNull(bundle)) {
            intent.putExtras(bundle)
        }

        if (flags != CODE_IGNORE) {
            intent.flags = flags
        }

        if (requestCode != CODE_IGNORE) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivity(intent)
        }
    }

    /**
     * 在fragment中打开 activity, 包括跨bundle
     *
     * @param fragment fragment
     * @param uri 正常情况下的class name.
     * @param bundle 业务数据
     * @param flags 跳转标记
     * @param requestCode 请求code
     */
    fun startActivity(fragment: Fragment, uri: String, bundle: Bundle?, flags: Int, requestCode: Int) {
        val intent = Intent()
        intent.setClassName(fragment.activity, uri)
        if (CheckUtil.isNotNull(bundle)) {
            intent.putExtras(bundle)
        }

        if (flags != CODE_IGNORE) {
            intent.flags = flags
        }

        if (requestCode != CODE_IGNORE) {
            fragment.startActivityForResult(intent, requestCode)
        } else {
            fragment.startActivity(intent)
        }
    }
}