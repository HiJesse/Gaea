package cn.jesse.gaea.lib.common.ui.activity

import android.text.TextUtils
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.common.R
import cn.jesse.gaea.lib.common.constant.PluginDef
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.nativelogger.NLogger
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.common_activity_pattern_lock.*

/**
 * 手势解锁页面
 *
 * @author Jesse
 */
class PatternLockActivity : BaseActivity() {
    private var patternSettingFirstTime: String? = null

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.PatternLockActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.common_activity_pattern_lock
    }

    override fun onActivityCreated() {
        RxPatternLockView.patternChanges(plvLock)
                .subscribe { event ->
                    handlePatternLockEvent(event)
                }
    }

    override fun setBackPressedEnable(): Boolean {
        return false
    }

    override fun setFullScreenEnable(): Boolean {
        return true
    }

    /**
     * 处理手势结果监听
     */
    private fun handlePatternLockEvent(event: PatternLockCompoundEvent) {
        when (event.eventType) {
            PatternLockCompoundEvent.EventType.PATTERN_STARTED -> {
                NLogger.i(mTag, "handlePatternLockEvent started")
            }
            PatternLockCompoundEvent.EventType.PATTERN_PROGRESS -> {
                NLogger.i(mTag, "handlePatternLockEvent progress")
            }
            PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> {
                NLogger.i(mTag, "handlePatternLockEvent complete ${event.pattern}")
                if (CheckUtil.isNull(event.pattern) || event.pattern!!.size <= 3) {
                    Toasty.normal(this, "手势最少包含四个点").show()
                    plvLock.clearPattern()
                    return
                }
                verifyPattern(PatternLockUtils.patternToMD5(plvLock, event.pattern))
            }
            PatternLockCompoundEvent.EventType.PATTERN_CLEARED -> {
                NLogger.i(mTag, "handlePatternLockEvent cleared")
            }
            else -> {
                // do nothing
            }
        }
    }

    /**
     * 校验用户手势, 根据不同的数据分出下列三种情况
     * 1. 设置手势
     * 2. 解锁成功
     * 3. 解锁失败
     */
    private fun verifyPattern(userPattern: String) {
        when {
            TextUtils.isEmpty(DataSetManager.getAppDataSet().patternLock) -> {
                setPattern(userPattern)
            }
            userPattern.equals(DataSetManager.getAppDataSet().patternLock, false) -> {
                Toasty.normal(this, "解锁成功").show()
                finish()
            }
            else -> {
                Toasty.normal(this, "解锁失败, 请重试").show()
                plvLock.clearPattern()
            }
        }
    }

    private fun setPattern(userPattern: String) {
        when {
            TextUtils.isEmpty(patternSettingFirstTime) -> {
                patternSettingFirstTime = userPattern
                Toasty.normal(this, "请再输出一次").show()
                plvLock.clearPattern()
            }
            patternSettingFirstTime.equals(userPattern) -> {
                DataSetManager.getAppDataSet().patternLock = userPattern
                Toasty.normal(this, "设置成功").show()
                finish()
            }
            else -> {
                Toasty.normal(this, "两次设置手势不匹配, 请重试").show()
                patternSettingFirstTime = null
                plvLock.clearPattern()
            }
        }
    }
}