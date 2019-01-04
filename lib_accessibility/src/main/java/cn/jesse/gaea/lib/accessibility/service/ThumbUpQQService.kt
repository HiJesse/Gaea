package cn.jesse.gaea.lib.accessibility.service

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import cn.jesse.gaea.lib.accessibility.constant.SPDef
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.TimeUtil
import cn.jesse.gaea.lib.common.util.SPUtil
import cn.jesse.nativelogger.NLogger

/**
 * QQ点赞辅助
 *
 * @author Jesse
 */
class ThumbUpQQService : BaseAccessibilityService() {
    private val TAG = "ThumbUpQQService"
    private val QQ_PAGE_SPLASH = "com.tencent.mobileqq.activity.SplashActivity"
    private val QQ_PAGE_SEARCH = "com.tencent.mobileqq.search.activity.UniteSearchActivity"
    private val QQ_PAGE_CHAT = "com.tencent.mobileqq.activity.ChatActivity"

    private val QQ_VIEW_ID_SEARCH = "com.tencent.mobileqq:id/et_search_keyword"
    private val QQ_VIEW_ID_PROFILE = "com.tencent.mobileqq:id/ivTitleBtnRightImage"
    private val nickname = "Jesse"

    private var thumbUpTimestamp = 0L


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)

        if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        if (isThumbUpped()) {
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

        if (eventClassName.equals(QQ_PAGE_CHAT, true)) {
            NLogger.i(TAG, "chat")
            openChatProfile()
        }
    }

    private fun saveTimestamp() {
        thumbUpTimestamp = TimeUtil.getCurrentTimestamp()
        SPUtil.appSP.setFlag(SPDef.KEY_QQ_THUMB_TIMESTAMP, thumbUpTimestamp)
    }

    /**
     * 判断当日是否已经点过赞了
     */
    private fun isThumbUpped(): Boolean {
        val savedDay = TimeUtil.getDayFromTimestamp(thumbUpTimestamp)
        return TimeUtil.getDayFromTimestamp(TimeUtil.getCurrentTimestamp()).equals(savedDay, true)
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

    /**
     * 打开对话用户详细信息并点赞10次, 最终返回桌面
     */
    private fun openChatProfile() {
        performViewClick(findViewByID(QQ_VIEW_ID_PROFILE))

        syncWait(500L)
        clickViewByText(nickname, true)
        syncWait(500L)

        for (i in 1..10) {
            clickViewByText("次赞", false)
            syncWait(500L)
        }

        sendMessage(GLOBAL_ACTION_HOME)
        saveTimestamp()
    }
}