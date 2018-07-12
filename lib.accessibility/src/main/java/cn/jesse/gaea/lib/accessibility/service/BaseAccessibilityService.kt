package cn.jesse.gaea.lib.accessibility.service

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.nativelogger.NLogger

/**
 * accessibility 基础服务
 *
 * @author Jesse
 */
open class BaseAccessibilityService : AccessibilityService() {
    private val TAG = "BaseAccessibilityService"
    private val DELAY_MESSAGE = 500L
    protected var eventType: Int = 0
    protected var eventClassName: String = ""
    protected var eventPackageName: String = ""
    protected lateinit var event: AccessibilityEvent


    private val performGlobalHandler = Handler { msg ->
        performGlobalAction(msg.what)
    }

    /**
     * 延时执行全局行为
     * GLOBAL_ACTION_BACK
     * GLOBAL_ACTION_HOME
     * GLOBAL_ACTION_RECENTS
     * GLOBAL_ACTION_NOTIFICATIONS
     * GLOBAL_ACTION_QUICK_SETTINGS
     * GLOBAL_ACTION_POWER_DIALOG
     * GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN
     *
     * @param action action
     */
    private fun sendMessage(action: Int) {
        performGlobalHandler.sendEmptyMessageDelayed(action, DELAY_MESSAGE)
    }

    /**
     * 同步等待 delay时长
     */
    protected fun syncWait(delay: Long) {
        try {
            Thread.sleep(delay)
        } catch (e: Exception) {
            NLogger.e(TAG, "syncWait ${e.message}")
        }
    }

    /**
     * 判断text是否能匹配到AccessibilityNodeInfo
     */
    private fun stringEquals(nodeInfo: AccessibilityNodeInfo, text: String): Boolean {
        if (CheckUtil.isNotNull(nodeInfo.text)) {
            return text.equals(nodeInfo.text.toString(), true)
        }

        if (CheckUtil.isNotNull(nodeInfo.contentDescription)) {
            return text.equals(nodeInfo.contentDescription.toString(), true)
        }

        if (CheckUtil.isNotNull(nodeInfo.contentDescription)) {
            return text.equals(nodeInfo.contentDescription.toString(), true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && CheckUtil.isNotNull(nodeInfo.hintText)) {
            return text.equals(nodeInfo.hintText.toString(), true)
        }

        return false
    }


    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    fun performViewClick(nodeInfo: AccessibilityNodeInfo?) {
        var nodeInfo: AccessibilityNodeInfo? = nodeInfo ?: return
        while (nodeInfo != null) {
            if (nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            nodeInfo = nodeInfo.parent
        }
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    @JvmOverloads
    fun findViewByText(text: String, clickable: Boolean = false): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        for (nodeInfo in nodeInfoList) {
            if (nodeInfo.isClickable == clickable && stringEquals(nodeInfo, text)) {
                return nodeInfo
            }
        }
        return null
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun findViewByID(id: String): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        for (nodeInfo in nodeInfoList) {
            return nodeInfo
        }
        return null
    }

    /**
     * 减少clickViewByText参数
     */
    fun clickViewByText(text: String, isEquals: Boolean) {
        clickViewByText(text, isEquals, true)
    }

    /**
     * 根据text点击当前view
     *
     * @param text 要超找view的text
     * @param isEquals 是: 必须相等 否: >= text
     * @param onlyOnce 是否只点击首个命中
     */
    fun clickViewByText(text: String, isEquals: Boolean, onlyOnce: Boolean) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)

        for (nodeInfo in nodeInfoList) {
            if (!isEquals || stringEquals(nodeInfo, text)) {
                performViewClick(nodeInfo)
                if (onlyOnce) return else break
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun clickViewByID(id: String) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        for (nodeInfo in nodeInfoList) {
            if (nodeInfo != null) {
                performViewClick(nodeInfo)
                break
            }
        }
    }

    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    fun inputText(nodeInfo: AccessibilityNodeInfo, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val arguments = Bundle()
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClip = ClipData.newPlainText("label", text)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        eventPackageName = event.packageName.toString()
        eventClassName = event.className.toString()
        eventType = event.eventType
        this.event = event
        NLogger.i(TAG, "onAccessibilityEvent " +
                "package: ${event.packageName} " +
                "class: ${event.className} " +
                "type: ${event.eventType} "
        )
    }

    override fun onInterrupt() {}
}