package cn.jesse.gaea.lib.base.util

import es.dmoral.toasty.Toasty

/**
 * 两次点击回调退出工具
 *
 * @author Jesse
 */
class DoubleExitUtil {
    private var startTime: Long = 0
    var message: CharSequence? = "再按一次退出APP"
    var delay: Int = 2000
    var listener: (() -> Unit)? = null

    /**
     * 点击调用, 内部判断是toast还是回调
     */
    fun click() {
        if (doInDelayTime() || CheckUtil.isNull(message)) {
            return
        }
        Toasty.normal(ContextUtil.getApplicationContext(), message!!).show()
    }

    /**
     * 判断当前点击是否满足条件回调
     *
     * @return false 不满足条件
     */
    private fun doInDelayTime(): Boolean {
        val nowTime = System.currentTimeMillis()
        if (nowTime - startTime <= delay) {
            if (CheckUtil.isNotNull(listener)) {
                listener!!()
            }
            startTime = 0
            return true
        }
        startTime = nowTime
        return false
    }

    companion object {
        @Volatile
        private var mInstance: DoubleExitUtil? = null

        fun getInstance(): DoubleExitUtil {
            if (mInstance == null) {
                synchronized(DoubleExitUtil::class.java) {
                    if (mInstance == null) {
                        mInstance = DoubleExitUtil()
                    }
                }
            }
            return mInstance!!
        }
    }
}