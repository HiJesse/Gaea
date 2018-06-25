package cn.jesse.gaea.lib.base.util

import cn.jesse.nativelogger.NLogger
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader

/**
 * 文件下载工具类
 *
 * @author Jesse
 */
object FileDownloaderUtil {
    private val TAG = "FileDownloaderUtil"

    fun download(url: String, path: String, succeedListener: (() -> Unit)?, errorListener: (() -> Unit)?) {
        download(url, path, succeedListener, errorListener, null)
    }

    /**
     * 下载文件
     *
     * @param url 下载地址
     * @param path 下载路径
     * @param succeedListener 成功回调
     * @param errorListener 错误回调
     * @param progressListener 进度回调百分比的整数分子
     */
    fun download(url: String, path: String, succeedListener: (() -> Unit)?, errorListener: (() -> Unit)?, progressListener: ((progress: Int) -> Unit)?) {
        FileDownloader.getImpl()
                .create(url)
                .setPath(path)
                .setListener(object : FileDownloadListener() {
                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        NLogger.d(TAG, "download paused $url $path")
                    }

                    override fun warn(task: BaseDownloadTask?) {
                        NLogger.d(TAG, "download warn $url $path")
                    }

                    override fun completed(task: BaseDownloadTask?) {
                        NLogger.d(TAG, "download completed $url $path")
                        if (CheckUtil.isNotNull(succeedListener)) {
                            succeedListener!!.invoke()
                        }
                    }

                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        NLogger.d(TAG, "download pending $url $path")
                    }

                    override fun error(task: BaseDownloadTask?, e: Throwable?) {
                        NLogger.e(TAG, "download error $url $path ${e?.message}")
                        if (CheckUtil.isNotNull(errorListener)) {
                            errorListener!!.invoke()
                        }
                    }

                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        NLogger.d(TAG, "download progress $url $path ${soFarBytes / totalBytes * 100}")
                        if (CheckUtil.isNotNull(progressListener)) {
                            progressListener!!.invoke(soFarBytes / totalBytes * 100)
                        }
                    }
                }).start()
    }
}