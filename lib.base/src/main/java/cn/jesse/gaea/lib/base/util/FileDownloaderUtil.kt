package cn.jesse.gaea.lib.base.util

import android.text.TextUtils
import cn.jesse.nativelogger.NLogger
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloadQueueSet
import com.liulishuo.filedownloader.FileDownloader

/**
 * 文件下载工具类
 *
 * @author Jesse
 */
object FileDownloaderUtil {
    private val TAG = "FileDownloaderUtil"

    /**
     * 下载单个文件, 提供简化成功失败回调
     */
    fun download(url: String, path: String, succeedListener: (() -> Unit)?, errorListener: (() -> Unit)?) {
        download(url, path, succeedListener, errorListener, null)
    }

    /**
     * 批量并行下载文件
     *
     * @param urls 下载链接数组
     * @param path 下载路径
     * @param succeedListener 成功回调
     * @param errorListener 错误回调
     */
    fun download(urls: Array<String?>, path: String, succeedListener: (() -> Unit)?, errorListener: (() -> Unit)?) {
        if (urls.isEmpty()) {
            return
        }

        val queueSet = FileDownloadQueueSet(object : FileDownloadListener() {
            override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                NLogger.d(TAG, "download paused ${task?.url} $path")
            }

            override fun warn(task: BaseDownloadTask?) {
                NLogger.d(TAG, "download warn ${task?.url} $path")
            }

            override fun completed(task: BaseDownloadTask?) {
                NLogger.d(TAG, "download completed ${task?.url} $path")
                if (CheckUtil.isNotNull(succeedListener)) {
                    succeedListener!!.invoke()
                }
            }

            override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                NLogger.d(TAG, "download pending ${task?.url} $path")
            }

            override fun error(task: BaseDownloadTask?, e: Throwable?) {
                NLogger.e(TAG, "download error ${task?.url} $path ${e?.message}")
                if (CheckUtil.isNotNull(errorListener)) {
                    errorListener!!.invoke()
                }
            }

            override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                NLogger.d(TAG, "download progress ${task?.url} $path ${soFarBytes / totalBytes * 100}")
            }
        })

        val tasks = ArrayList<BaseDownloadTask>()

        for (url in urls) {
            if (TextUtils.isEmpty(url)) {
                continue
            }
            tasks.add(FileDownloader.getImpl().create(url).setPath(path, true).setTag(urls.indexOf(url)))
        }
        queueSet.disableCallbackProgressTimes()
        queueSet.setAutoRetryTimes(1)
        queueSet.downloadTogether(tasks)
        queueSet.start()
    }

    /**
     * 下载单个文件
     *
     * @param url 下载地址
     * @param path 下载文件名称
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