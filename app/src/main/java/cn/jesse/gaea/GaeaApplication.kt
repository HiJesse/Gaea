package cn.jesse.gaea

import android.app.Application
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.constant.Urls
import cn.jesse.gaea.lib.network.HttpEngine
import cn.jesse.nativelogger.NLogger
import cn.jesse.nativelogger.NLoggerConfig
import cn.jesse.nativelogger.formatter.SimpleFormatter
import cn.jesse.nativelogger.logger.LoggerLevel
import es.dmoral.toasty.Toasty
import org.osgi.framework.BundleException
import java.io.File

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

        // 设置加载远程bundle回调
        Atlas.getInstance().setClassNotFoundInterceptorCallback(ClassNotFoundInterceptorCallback { intent ->
            val className = intent.component!!.className
            val bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className)

            if (TextUtils.isEmpty(bundleName) || AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {
                return@ClassNotFoundInterceptorCallback intent
            }

            //远程bundle
            val activity = ActivityTaskMgr.getInstance().peekTopActivity()
            val remoteBundleFile = File(activity.externalCacheDir, "lib" + bundleName.replace(".", "_") + ".so")

            var path = ""
            if (remoteBundleFile.exists()) {
                path = remoteBundleFile.absolutePath
            } else {
                Toasty.normal(this, "插件不存在，请确定 : ${remoteBundleFile.absolutePath}").show()
                NLogger.e("插件不存在，请确定 : ${remoteBundleFile.absolutePath}")
                return@ClassNotFoundInterceptorCallback intent
            }


            val info = activity.packageManager.getPackageArchiveInfo(path, 0)
            try {
                Atlas.getInstance().installBundle(info.packageName, File(path))
            } catch (e: BundleException) {
                Toasty.normal(this, "插件安装失败, 请确定 : ${e.message}").show()
                NLogger.e("插件安装失败, 请确定 : ${e.message}")
            }

            activity.startActivities(arrayOf(intent))

            intent
        })
    }
}