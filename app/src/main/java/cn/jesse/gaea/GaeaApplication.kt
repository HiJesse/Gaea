package cn.jesse.gaea

import android.app.Application
import cn.jesse.nativelogger.NLogger
import cn.jesse.nativelogger.NLoggerConfig
import cn.jesse.nativelogger.formatter.SimpleFormatter
import cn.jesse.nativelogger.logger.LoggerLevel

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
    }
}