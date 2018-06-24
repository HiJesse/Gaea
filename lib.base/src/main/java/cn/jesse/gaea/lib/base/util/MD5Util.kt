package cn.jesse.gaea.lib.base.util

import android.text.TextUtils
import cn.jesse.nativelogger.NLogger
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5 相关工具
 *
 * @author Jesse
 */
object MD5Util {
    private val TAG = MD5Util::class.java.simpleName

    /**
     * 获取字符串MD5
     *
     * @param str 待加密字符串
     * @return 加密后md5
     */
    fun getMD5(str: String): String {
        val md: MessageDigest
        var md5 = ""
        try {
            md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            md5 = BigInteger(1, md.digest()).toString(16)
        } catch (e: Exception) {
            NLogger.e(TAG, "getMD5 ${e.message}")
        }

        return md5
    }

    /**
     * 获取文件MD5
     *
     * @param filePath 文件路径
     * @return 文件MD5
     */
    fun getFileMD5(filePath: String): String {
        var fileMD5 = ""
        var fis: FileInputStream? = null
        try {
            val file = File(filePath)
            fis = FileInputStream(file)
            val md = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(1024)
            var length: Int = fis.read(buffer, 0, 1024)
            while (length != -1) {
                md.update(buffer, 0, length)
                length = fis.read(buffer, 0, 1024)
            }
            val bigInt = BigInteger(1, md.digest())
            fileMD5 = bigInt.toString(16)
        } catch (e: NoSuchAlgorithmException) {
            NLogger.e(TAG, "getFileMD5 ${e.message}")
        } catch (e: IOException) {
            NLogger.e(TAG, "getFileMD5 ${e.message}")
        } finally {
            IOUtil.close(fis)
        }
        return fileMD5
    }

    /**
     * 校验文件的md5是否匹配
     *
     * @param filePath 文件路径
     * @param md5      预匹配的md5
     * @return 是否匹配
     */
    fun compareFileMD5(filePath: String, md5: String): Boolean {
        var isMatch = false
        val fileMD5 = getFileMD5(filePath)

        if (!TextUtils.isEmpty(fileMD5) && !TextUtils.isEmpty(md5) && fileMD5 == md5) {
            isMatch = true
        }
        return isMatch
    }

    /**
     * 校验字符串的md5是否匹配
     *
     * @param str 字符串
     * @param md5 预匹配的md5
     * @return 是否匹配
     */
    fun compareMD5(str: String, md5: String): Boolean {
        var isMatch = false
        val strMd5 = getMD5(str)

        if (!TextUtils.isEmpty(strMd5) && !TextUtils.isEmpty(md5) && strMd5 == md5) {
            isMatch = true
        }
        return isMatch
    }
}