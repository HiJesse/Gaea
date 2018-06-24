package cn.jesse.gaea.lib.base.util

import cn.jesse.nativelogger.NLogger
import java.io.Closeable

/**
 * IO相关操作工具
 *
 * @author Jesse
 */
object IOUtil {
    private val TAG = IOUtil::class.java.simpleName

    fun close(vararg closeables: Closeable?) {
        if (CheckUtil.isNull(closeables)) {
            return
        }
        for (closeable in closeables) {
            try {
                closeable!!.close()
            } catch (e: Exception) {
                NLogger.e(TAG, "close ${e.message}")
            }

        }
    }
}