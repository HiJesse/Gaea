package cn.jesse.gaea.lib.common.util

import android.content.Context
import cn.jesse.gaea.lib.base.manager.SharedPreferenceManager
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.constant.SPDef

/**
 * sp 工具类, 业务相关
 *
 * @author Jesse
 */
object SPUtil {

    // 应用信息
    val appSP = SharedPreferenceManager(
            ContextUtil.getApplicationContext(),
            SPDef.NAME_APP,
            Context.MODE_PRIVATE
    )

    // 用户信息
    val userSP = SharedPreferenceManager(
            ContextUtil.getApplicationContext(),
            SPDef.NAME_USER,
            Context.MODE_PRIVATE
    )

    /**
     * 清除sp相关信息
     */
    fun clear() {
        userSP.clear()
        appSP.clear()
    }
}