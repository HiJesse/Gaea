package cn.jesse.gaea.lib.common.util

import android.app.Activity
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.nativelogger.NLogger
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.WaitDialog
import es.dmoral.toasty.Toasty
import org.osgi.framework.BundleException
import java.io.File

/**
 * atlas bundle相关工具
 *
 * @author Jesse
 */
object AtlasBundleUtil {
    private val TAG = "AtlasBundleUtil"

    // class not fount 回调
    val classNotFoundInterceptorCallback = ClassNotFoundInterceptorCallback { intent ->
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
            showDownloadBundleDialog(activity, bundleName)
            NLogger.e("插件不存在，请确定 : ${remoteBundleFile.absolutePath}")
            return@ClassNotFoundInterceptorCallback intent
        }


        val info = activity.packageManager.getPackageArchiveInfo(path, 0)

        try {
            Atlas.getInstance().installBundle(info.packageName, File(path))
        } catch (e: BundleException) {
            Toasty.normal(ContextUtil.getApplicationContext(), "插件安装失败 : ${e.message}").show()
            NLogger.e(TAG, "插件安装失败, : ${e.message}")
        }

        activity.startActivities(arrayOf(intent))

        intent
    }

    /**
     * 显示是否下载插件对话框
     */
    private fun showDownloadBundleDialog(activity: Activity, bundleName: String) {
        val bundleInfo = DataSetManager.getAppDataSet().getBundleInfo(bundleName)
        if (CheckUtil.isNull(bundleInfo)) {
            NLogger.e(TAG, "showDownloadBundleDialog bundle info is null")
            return
        }

        SelectDialog.show(activity, "插件不存在", "是否下载插件 ?", "确定", { _, _ ->
            WaitDialog.show(activity, "下载中")
        }, "取消", { _, _ ->
            // unused
        })
    }

}