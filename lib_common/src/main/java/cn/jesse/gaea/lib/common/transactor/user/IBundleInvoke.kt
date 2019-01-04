package cn.jesse.gaea.lib.common.transactor.user

import android.content.Context

/**
 * 测试跨模块方法调起 (非业务, 测试)
 *
 * @author Jesse
 */
interface IBundleInvoke {
    /**
     * 测试跨bundle toast
     */
    fun toast(context: Context, msg: String)
}