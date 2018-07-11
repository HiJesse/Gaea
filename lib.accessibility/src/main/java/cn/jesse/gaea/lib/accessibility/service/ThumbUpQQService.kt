package cn.jesse.gaea.lib.accessibility.service

import android.view.accessibility.AccessibilityEvent



class ThumbUpQQService : BaseAccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)
        if ("com.tencent.mobileqq".equals(event.packageName.toString(), true)) {
            val nodeInfo = findViewByText("联系人", true)
            if (nodeInfo != null) {
                performViewClick(nodeInfo)
            }
        }
    }
}