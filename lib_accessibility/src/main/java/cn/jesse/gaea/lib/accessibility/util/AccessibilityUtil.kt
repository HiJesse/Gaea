package cn.jesse.gaea.lib.accessibility.util

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.nativelogger.NLogger

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
        val context = ContextUtil.getApplicationContext()
        val serviceStr = "${context.packageName}/$serviceName"

        var accessibilityEnabled = 0
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            NLogger.e(TAG, "checkAccessibilityEnabled ${e.message}")
        }

        if (accessibilityEnabled == 0) {
            return false
        }

        val settingValue = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

        if (CheckUtil.isNull(settingValue)) {
            return false
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        mStringColonSplitter.setString(settingValue)
        while (mStringColonSplitter.hasNext()) {
            val accessibilityService = mStringColonSplitter.next()

            if (accessibilityService.equals(serviceStr, true)) {
                return true
            }
        }

        return false
    }
}