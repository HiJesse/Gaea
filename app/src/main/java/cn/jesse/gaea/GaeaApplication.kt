package cn.jesse.gaea

import android.app.Application
import android.taobao.atlas.framework.Atlas
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.constant.Urls
import cn.jesse.gaea.lib.common.util.AtlasBundleUtil
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.nativelogger.NLogger
import cn.jesse.nativelogger.NLoggerConfig
import cn.jesse.nativelogger.formatter.SimpleFormatter
import cn.jesse.nativelogger.logger.LoggerLevel
import com.kongzue.dialog.v2.DialogSettings
import com.liulishuo.filedownloader.FileDownloader

/**
 * 入口application. 由于使用了atlas, 并不是真正意义上的入口
 */
class GaeaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initService()
    }

    /**
     * 初始化各种服务
     */
    private fun initService() {
        // 初始化application context
        ContextUtil.init(this)
        // 初始化dialog
        DialogSettings.type = DialogSettings.TYPE_IOS
        DialogSettings.use_blur = false
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT
        // 初始化日志
        NLoggerConfig.getInstance()
                .builder()
                .tag("GAEA")
                .loggerLevel(LoggerLevel.DEBUG)
                .fileLogger(true)
                .fileDirectory(applicationContext.filesDir.path + "/logs")
                .fileFormatter(SimpleFormatter())
                .expiredPeriod(3)
                .catchException(true, { _, ex ->
                    NLogger.e("uncaughtException", ex!!)
                    android.os.Process.killProcess(android.os.Process.myPid())
                })
                .build()

        // 初始化网络服务
        val httpHeaders = mapOf(Pair("App", "Gaea"))
        HttpEngine.getInstance()
                .setBaseUrl(Urls.baseUrl)
                .setTimeout(3000L, 3000L)
                .setHttpHeader(httpHeaders)
                .build()

        // 初始化File Downloader
        FileDownloader.setupOnApplicationOnCreate(this)

        // 设置加载远程bundle回调
        Atlas.getInstance().setClassNotFoundInterceptorCallback(AtlasBundleUtil.classNotFoundInterceptorCallback)
    }
}