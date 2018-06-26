package cn.jesse.gaea.lib.common.util

import android.app.Activity
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.*
import cn.jesse.gaea.lib.common.bean.RemoteBundleInfoBean
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.nativelogger.NLogger
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
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
        val remoteBundleFile = File(WorkspaceUtil.getInstance().bundleFiles, "lib" + bundleName.replace(".", "_") + ".so")

        var path = ""
        if (remoteBundleFile.exists()) {
            path = remoteBundleFile.absolutePath
        } else {
            showDownloadBundleDialog(activity, bundleName)
            NLogger.e("插件不存在，请确定 : ${remoteBundleFile.absolutePath}")
            return@ClassNotFoundInterceptorCallback intent
        }


        val info = activity.packageManager.getPackageArchiveInfo(path, 0)

        installBundle(info.packageName, path)

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
           downloadBundle(activity, bundleName, bundleInfo!!)
        }, "取消", { _, _ ->
            // unused
        })
    }

    /**
     * 1. 下载bundle
     * 2. 校验bundle md5
     * 3. 校验bundle签名
     */
    private fun downloadBundle(activity: Activity, bundleName: String, bundleInfo: RemoteBundleInfoBean) {
        val remoteBundleFile = File(WorkspaceUtil.getInstance().bundleFiles, "lib" + bundleName.replace(".", "_") + ".so")
        WaitDialog.show(activity, "下载中")
        FileDownloaderUtil.download(bundleInfo.bundleDownloadUrl!!, remoteBundleFile.absolutePath, {

            if (!MD5Util.compareFileMD5(remoteBundleFile.absolutePath, bundleInfo.bundleFileMD5!!)) {
                TipDialog.show(activity, "安装失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
                return@download
            }

            WaitDialog.show(activity, "安装中")
            val installStatus = installBundle(bundleName, remoteBundleFile.absolutePath)
            WaitDialog.dismiss()
            if (installStatus) {
                TipDialog.show(activity, "安装成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
            } else {
                TipDialog.show(activity, "安装失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
            }
        }, {
            WaitDialog.dismiss()
            TipDialog.show(activity, "下载失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
        })
    }

    /**
     * 安装bundle
     *
     * @param bundleName 要安装的bundle名称
     * @param path bundle物理文件路径
     */
    fun installBundle(bundleName: String, path: String): Boolean {
        var succeed = false
        try {
            Atlas.getInstance().installBundle(bundleName, File(path))
            succeed = true
        } catch (e: BundleException) {
            Toasty.normal(ContextUtil.getApplicationContext(), "插件安装失败 : ${e.message}").show()
            NLogger.e(TAG, "插件安装失败, : ${e.message}")
        }
        return succeed
    }

}