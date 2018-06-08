package cn.jesse.gaea.lib.base.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Process
import cn.jesse.nativelogger.NLogger

/**
 * app相关工具
 *
 * @author Jesse
 */
object AppUtil {
    private val TAG = "AppUtil"

    /**
     * 判断是否为UI主进程
     *
     * @param context context
     * @return 是否为主进程
     */
    fun isAppMainProcess(context: Context): Boolean {
        var isAppMainProcess = false
        val pid = android.os.Process.myPid()
        val process = getAppNameByPID(context.applicationContext, pid)

        if (getPackageName(context) == process) {
            isAppMainProcess = true
        }

        return isAppMainProcess
    }

    /**
     * 根据Pid得到进程名
     *
     * @param context context
     * @param pid     进程id
     * @return 进程名
     */
    fun getAppNameByPID(context: Context, pid: Int): String {
        val manager = context.applicationContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }

    /**
     * 获取包名
     *
     * @param context context
     * @return 包名
     */
    fun getPackageName(context: Context?): String {
        if (CheckUtil.isNull(context)) {
            return ""
        }

        return context!!.packageName
    }

    /**
     * 判断是否是debug
     *
     * @param context context
     * @return true /false
     */
    fun isDebugVersion(context: Context?): Boolean {
        var isDebug = false
        if (CheckUtil.isNull(context)) {
            return false
        }

        val pkgName = getPackageName(context)
        var packageInfo: PackageInfo? = null
        try {

            packageInfo = context!!.packageManager.getPackageInfo(
                    pkgName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            NLogger.e(TAG, e.message!!)
        }

        if (CheckUtil.isNotNull(packageInfo)) {
            val info = packageInfo!!.applicationInfo
            isDebug = info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
        return isDebug
    }

    /**
     * 获取应用version name
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context?): String {
        var versionName = "-1"

        if (CheckUtil.isNull(context)) {
            return versionName
        }

        val manager = context!!.applicationContext.packageManager
        try {
            val info = manager.getPackageInfo(context.applicationContext.packageName, 0)
            versionName = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            NLogger.e(TAG, e.message!!)
        }

        return versionName
    }

    /**
     * 获取应用version code
     *
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): Int {
        var versionCode = -1
        val manager = context.applicationContext.packageManager
        try {
            val info = manager.getPackageInfo(context.applicationContext.packageName, 0)
            versionCode = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            NLogger.e(TAG, e.message!!)
        }

        return versionCode
    }

    /**
     * 判断当前应用是否在后台运行
     *
     * @param context context
     * @return
     */
    fun isBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
            }
        }
        return false
    }

    /**
     * 退出进程
     */
    fun exitProcess() {
        Process.killProcess(Process.myPid())
    }
}