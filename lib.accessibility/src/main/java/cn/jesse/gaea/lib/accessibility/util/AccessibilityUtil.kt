package cn.jesse.gaea.lib.accessibility.util

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import cn.jesse.gaea.lib.base.util.ContextUtil

/**
 * accessibility 工具
 *
 * @author Jesse
 */
object AccessibilityUtil {
    val TAG = "AccessibilityUtil"

    /**
     * 前往开启辅助服务界面
     */
    fun gotoAccessPage(activity: Activity) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    fun checkAccessibilityEnabled(serviceName: String): Boolean {
        val mAccessibilityManager: AccessibilityManager= ContextUtil.getApplicationContext()
                .getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        val accessibilityServices = mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (info in accessibilityServices) {
            if (info.id == serviceName) {
                return true
            }
        }
        return false
    }
}