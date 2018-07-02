package cn.jesse.gaea.lib.common.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.opengl.Visibility
import android.view.View
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.common.R
import cn.jesse.gaea.lib.common.bean.PatternLockStatus
import cn.jesse.gaea.lib.common.constant.PluginDef
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.vm.PatternLockViewModel
import com.andrognito.rxpatternlockview.RxPatternLockView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.common_activity_pattern_lock.*

/**
 * 手势解锁页面
 *
 * @author Jesse
 */
class PatternLockActivity : BaseActivity() {
    private lateinit var lockViewModel: PatternLockViewModel
    private var closeable = false
    private val lockResultObserver = Observer<PatternLockStatus> { result ->
        handleLockResult(result)
    }

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.PatternLockActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.common_activity_pattern_lock
    }

    override fun onActivityCreated() {
        lockViewModel = ViewModelProviders.of(this).get(PatternLockViewModel::class.java)
        lockViewModel.lockResult.observe(this, lockResultObserver)

        if (CheckUtil.isNotNull(intent.extras)) {
            closeable = intent.extras.getBoolean(RemoteRouterDef.LibCommon.PARAMS_LOCK_CLOSEABLE, false)
        }

        if (closeable) {
            ivClose.visibility = View.VISIBLE
            ivClose.setOnClickListener {
                finish()
            }
        }

        lockViewModel.setDotCount(plvLock.dotCount)
        lockViewModel.getPatternLockStatus()
        RxPatternLockView.patternChanges(plvLock)
                .subscribe { event ->
                    lockViewModel.handlePatternLockEvent(event)
                }
    }

    override fun setBackPressedEnable(): Boolean {
        return closeable
    }

    override fun setFullScreenEnable(): Boolean {
        return true
    }

    /**
     * 处理view model的结果
     */
    private fun handleLockResult(result: PatternLockStatus?) {
        if (CheckUtil.isNull(result)) {
            return
        }

        when (result) {
            PatternLockStatus.INIT_SETTED -> {
                setHint("请输入正确的手势密码", false)
            }
            PatternLockStatus.INIT_UNSET -> {
                setHint("请设置手势密码", false)
            }
            PatternLockStatus.SUCCEED_FIRST -> {
                setHint("请再输出一次", false)
                plvLock.clearPattern()
            }
            PatternLockStatus.SUCCEED_SECOND -> {
                Toasty.normal(this, "设置成功").show()
                finish()
            }
            PatternLockStatus.SUCCEED_UNLOCK -> {
                Toasty.normal(this, "解锁成功").show()
                finish()
            }
            PatternLockStatus.ERROR_PATTERN -> {
                setHint("手势最少包含四个点", true)
                plvLock.clearPattern()
            }
            PatternLockStatus.ERROR_UNLOCK -> {
                setHint("解锁失败, 请重试", true)
                plvLock.clearPattern()
            }
            PatternLockStatus.ERROR_DOUBLE_CHECK -> {
                setHint("两次设置手势不匹配, 请重试", true)
                plvLock.clearPattern()
            }
        }
    }

    private fun setHint(hint: String, isError: Boolean) {
        tvHint.text = hint

        if (isError) {
            tvHint.setTextColor(Color.RED)
        } else {
            tvHint.setTextColor(Color.BLUE)
        }
    }
}