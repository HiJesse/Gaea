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

    /**
     * 校验两个字符串是否相等
     */
    fun isStringEquals(origin: String?, match: String?): Boolean {
        return when (isNull(origin)) {
            true -> origin == match
            origin.equals(match) -> true
            else -> false
        }
    }
}