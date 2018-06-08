package cn.jesse.gaea.lib.base.util

/**
 * 字符串相关工具
 *
 * @author Jesse
 */
object StringUtil {

    /**
     * str 是否为null 或空字符串
     *
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    fun isEmpty(str: String?): Boolean {
        return CheckUtil.isNull(str) || str!!.isEmpty()
    }

    /**
     * str 是否不为null并且不是空字符串
     *
     * @return {@code true}: 不为空<br> {@code false}: 为空
     */
    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return `true`: 相等<br></br>`false`: 不相等
     */
    fun equals(a: String?, b: String?): Boolean {
        if (CheckUtil.isNull(a)) {
            return CheckUtil.isNull(b)
        }
        return a.equals(b)
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    fun length(s: String?): Int {
        if (isEmpty(s)) {
            return 0
        }
        return s!!.length
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    fun reverse(s: String): String {
        if (isEmpty(s)) {
            return s
        }
        val len = length(s)
        if (len <= 1) {
            return s
        }
        val mid = len shr 1
        val chars = s.toCharArray()
        var c: Char
        for (i in 0 until mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    fun toDBC(s: String): String {
        if (isEmpty(s)) {
            return s
        }
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            when (chars[i].toInt()) {
                12288 -> chars[i] = ' '
                in 65281..65374 -> chars[i] = (chars[i].toInt() - 65248).toChar()
                else -> chars[i] = chars[i]
            }
            i++
        }
        return String(chars)
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    fun toSBC(s: String): String {
        if (isEmpty(s)) {
            return s
        }
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            when {
                chars[i] == ' ' -> chars[i] = 12288.toChar()
                chars[i].toInt() in 33..126 -> chars[i] = (chars[i].toInt() + 65248).toChar()
                else -> chars[i] = chars[i]
            }
            i++
        }
        return String(chars)
    }
}