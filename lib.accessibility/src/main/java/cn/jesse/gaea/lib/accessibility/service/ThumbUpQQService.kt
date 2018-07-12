package cn.jesse.gaea.lib.accessibility.service

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.nativelogger.NLogger

/**
 * 辅助
 *
 * @author Jesse
 */
class ThumbUpQQService : BaseAccessibilityService() {
    private val TAG = "ThumbUpQQService"
    private val QQ_PAGE_SPLASH = "com.tencent.mobileqq.activity.SplashActivity"
    private val QQ_PAGE_SEARCH = "com.tencent.mobileqq.search.activity.UniteSearchActivity"
    private val QQ_VIEW_ID_SEARCH = "com.tencent.mobileqq:id/et_search_keyword"
    private val nickname = "Jesse"


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)

        if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        if (eventClassName.equals(QQ_PAGE_SPLASH, true)) {
            NLogger.i(TAG, "splash")
            clickContact()
        }

        if (eventClassName.equals(QQ_PAGE_SEARCH, true)) {
            NLogger.i(TAG, "search")
            searchFriend()
        }
    }

    /**
     * 点击主页的联系人页面并进入搜索页面
     */
    private fun clickContact() {
        val contact = findViewByText("联系人", false)
        contact?.parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        clickViewByText("搜索", true)
    }

    /**
     * 根据昵称搜索特定的用户并点击进入
     */
    private fun searchFriend() {
        val editSearch = findViewByID(QQ_VIEW_ID_SEARCH)

        if (CheckUtil.isNull(editSearch)) {
            NLogger.e(TAG, "search edit view is null")
            return
        }

        inputText(editSearch!!, nickname)

        syncWait(1000L)
        clickViewByText("来自分组:", false)
    }
}