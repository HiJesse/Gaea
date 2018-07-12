package cn.jesse.gaea.lib.base.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间相关工具
 *
 * @author Jesse
 */
object TimeUtil {
    val LONG_SECOND = 1000
    val LONG_MINUTE = 60 * LONG_SECOND
    val LONG_HOUR = 60 * LONG_MINUTE
    val LONG_DAY = 24 * LONG_HOUR

    /**
     * 获取当前时间
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取从时间戳开始到现在的天数
     */
    fun getDayFromNow(time: Long): Long {
        return (getCurrentTimestamp() - time) / LONG_DAY
    }

    /**
     * 从时间戳里获取天
     */
    fun getDayFromTimestamp(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val sf = SimpleDateFormat("dd")

        return sf.format(calendar.time)
    }
}