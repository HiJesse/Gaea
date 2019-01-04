package cn.jesse.gaea.lib.base.util

import android.annotation.SuppressLint
import android.content.Context

/**
 * application context 工具类
 *
 * @author Jesse
 */
@SuppressLint("StaticFieldLeak")
object ContextUtil {
    private var context: Context? = null

    /**
     * application 初始化时 设置
     *
     * @param applicationContext application的context
     */
    fun init(applicationContext: Context) {
        this.context = applicationContext
    }

    /**
     * 获取application context, 如果为空则跑出NPE异常
     */
    fun getApplicationContext(): Context {
        if (CheckUtil.isNull(context)) {
            throw NullPointerException("u should init first")
        }

        return context!!
    }
}