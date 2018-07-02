package cn.jesse.gaea.lib.common.vm

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.MD5Util
import cn.jesse.gaea.lib.base.vm.BaseViewModel
import cn.jesse.gaea.lib.common.bean.PatternLockStatus
import cn.jesse.gaea.lib.common.constant.PluginDef
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.nativelogger.NLogger
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent

/**
 * 手势解锁
 *
 * @author Jesse
 */
class PatternLockViewModel : BaseViewModel() {
    private val TAG = "${PluginDef.TAG}.PatternLockViewModel"

    private var dotCount = 3
    val lockResult: MutableLiveData<PatternLockStatus> = MutableLiveData()

    private var patternSettingFirstTime: String? = null

    /**
     * 设置手势密码点数
     */
    fun setDotCount(count: Int) {
        if (count == 0) {
            return
        }

        dotCount = count
    }

    /**
     * 获取手势密码相关状态
     */
    fun getPatternLockStatus() {
        if (hasStashedPattern()) {
            lockResult.value = PatternLockStatus.INIT_SETTED
        } else {
            lockResult.value = PatternLockStatus.INIT_UNSET
        }
    }

    /**
     * 处理手势回调逻辑
     */
    fun handlePatternLockEvent(event: PatternLockCompoundEvent) {
        when (event.eventType) {
            PatternLockCompoundEvent.EventType.PATTERN_STARTED -> {
                NLogger.i(TAG, "handlePatternLockEvent started")
            }
            PatternLockCompoundEvent.EventType.PATTERN_PROGRESS -> {
                NLogger.i(TAG, "handlePatternLockEvent progress")
            }
            PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> {
                NLogger.i(TAG, "handlePatternLockEvent complete ${event.pattern}")
                if (CheckUtil.isNull(event.pattern) || event.pattern!!.size <= 3) {
                    lockResult.value = PatternLockStatus.ERROR_PATTERN
                    return
                }
                verifyPattern(MD5Util.getMD5(patternToString(event.pattern)))
            }
            PatternLockCompoundEvent.EventType.PATTERN_CLEARED -> {
                NLogger.i(TAG, "handlePatternLockEvent cleared")
            }
            else -> {
                // do nothing
            }
        }
    }

    /**
     * 本地是否有暂存的手势
     */
    private fun hasStashedPattern(): Boolean {
        return !TextUtils.isEmpty(DataSetManager.getAppDataSet().patternLock)
    }

    /**
     * 校验用户手势, 根据不同的数据分出下列三种情况
     * 1. 设置手势
     * 2. 解锁成功
     * 3. 解锁失败
     */
    private fun verifyPattern(userPattern: String) {
        when {
            !hasStashedPattern() -> {
                setPattern(userPattern)
            }
            userPattern.equals(DataSetManager.getAppDataSet().patternLock, false) -> {
                lockResult.value = PatternLockStatus.SUCCEED_UNLOCK
            }
            else -> {
                lockResult.value = PatternLockStatus.ERROR_UNLOCK
            }
        }
    }

    /**
     * 设置手势
     * 1. 第一次设置成功
     * 2. 第二次设置成功
     * 3. 第二次设置和第一次设置不匹配
     */
    private fun setPattern(userPattern: String) {
        when {
            TextUtils.isEmpty(patternSettingFirstTime) -> {
                patternSettingFirstTime = userPattern
                lockResult.value = PatternLockStatus.SUCCEED_FIRST
            }
            patternSettingFirstTime.equals(userPattern) -> {
                DataSetManager.getAppDataSet().patternLock = userPattern
                lockResult.value = PatternLockStatus.SUCCEED_SECOND
            }
            else -> {
                patternSettingFirstTime = null
                lockResult.value = PatternLockStatus.ERROR_DOUBLE_CHECK
            }
        }
    }

    /**
     * 将手势转化为字符串
     */
    private fun patternToString(pattern: List<PatternLockView.Dot>?): String {
        if (pattern == null) {
            return ""
        }
        val patternSize = pattern.size
        val stringBuilder = StringBuilder()

        for (i in 0 until patternSize) {
            val dot = pattern[i]
            stringBuilder.append(dot.row * dotCount + dot.column)
        }
        return stringBuilder.toString()
    }
}