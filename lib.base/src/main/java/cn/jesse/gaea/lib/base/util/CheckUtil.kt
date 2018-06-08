package cn.jesse.gaea.lib.base.util

/**
 * 各种检查工具类
 *
 * @author Jesse
 */
object CheckUtil {

    /**
     * 对象any是否为null
     */
    fun isNull(any: Any?): Boolean {
        return any == null
    }

    /**
     * 对象any是否不为空
     */
    fun isNotNull(any: Any?): Boolean {
        return !isNull(any)
    }
}