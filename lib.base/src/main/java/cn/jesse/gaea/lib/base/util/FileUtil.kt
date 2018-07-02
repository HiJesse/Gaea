package cn.jesse.gaea.lib.base.util

import cn.jesse.nativelogger.NLogger
import java.io.File

/**
 * 文件相关工具
 *
 * @author Jesse
 */
object FileUtil {
    private val TAG = "FileUtil"

    /**
     * 删除指定文件
     */
    fun deleteFile(path: String) {
        val file = File(path)

        if (!file.exists()) {
            NLogger.e(TAG, "deleteFile file is not exist")
            return
        }

        try {
            file.delete()
        } catch (e: Exception) {
            NLogger.e(TAG, "deleteFile ${e.message}")
        }
    }
}