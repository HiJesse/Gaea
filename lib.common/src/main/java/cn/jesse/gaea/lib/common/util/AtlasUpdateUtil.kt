package cn.jesse.gaea.lib.common.util

import android.app.Activity
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.framework.Framework
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import cn.jesse.gaea.lib.base.util.*
import cn.jesse.gaea.lib.common.bean.RemoteBundleInfoBean
import cn.jesse.gaea.lib.common.dataset.DataSetManager
import cn.jesse.gaea.lib.network.transformer.IOMainThreadTransformer
import cn.jesse.nativelogger.NLogger
import com.google.gson.Gson
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.taobao.atlas.dex.util.FileUtils
import com.taobao.atlas.update.AtlasUpdater
import com.taobao.atlas.update.model.UpdateInfo
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import org.osgi.framework.BundleException
import java.io.File

/**
 * atlas bundle相关工具
 *
 * @author Jesse
 */
object AtlasUpdateUtil {
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
            NLogger.e("classNotFoundInterceptorCallback 插件不存在，请确定 : ${remoteBundleFile.absolutePath}")
            return@ClassNotFoundInterceptorCallback intent
        }


        val info = activity.packageManager.getPackageArchiveInfo(path, 0)

        installBundle(info.packageName, path)

        activity.startActivities(arrayOf(intent))

        intent
    }

    /**
     * 安装bundle, 安装成功后删除本地bundle文件
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
            NLogger.e(TAG, "installBundle 插件安装失败, : ${e.message}")
        }

        if (succeed) {
            FileUtil.deleteFile(path)
        }

        return succeed
    }

    /**
     * 卸载bundle
     *
     * @param bundleName 要卸载的bundle名称
     */
    fun uninstallBundle(bundleName: String) {
        if (CheckUtil.isNull(Framework.getBundle(bundleName))) {
            NLogger.i(TAG, "uninstallBundle $bundleName is not exist")
            return
        }

        Atlas.getInstance().uninstallBundle(bundleName)
    }

    /**
     * 加载T patch
     *
     * @param jsonFilePath 配置文件路径
     * @param patchFilePath 补丁文件路径
     * @param patchMD5 补丁文件MD5
     * @param listener 加载成功失败 UI线程回调
     */
    fun loadTPatch(jsonFilePath: String, patchFilePath: String, patchMD5: String, listener: ((status: Boolean) -> Unit)) {
        Observable.just(true)
                .map {
                    AtlasUpdateUtil.loadTPatch(jsonFilePath, patchFilePath, patchMD5)
                }
                .compose(IOMainThreadTransformer())
                .subscribe(listener)
    }

    /**
     * 加载patch并安装 (安装过程会比较慢, 一定不要在UI线程操作)
     *
     * @param jsonFilePath 配置文件路径
     * @param patchFilePath 补丁文件路径
     * @param patchMD5 补丁文件MD5
     */
    private fun loadTPatch(jsonFilePath: String, patchFilePath: String, patchMD5: String): Boolean {
        val updateInfo = File(jsonFilePath)
        val patchFile = File(patchFilePath)

        if (!updateInfo.exists()) {
            NLogger.e(TAG, "loadTPatch update json file is not exist")
            return false
        }

        if (!patchFile.exists()) {
            NLogger.e(TAG, "loadTPatch update patch file is not exist")
            return false
        }

        if (!MD5Util.compareFileMD5(patchFilePath, patchMD5)) {
            NLogger.e(TAG, "loadTPatch patch file MD5 is not match")
            return false
        }

        try {
            val jsonStr = String(FileUtils.readFile(updateInfo))
            val info = Gson().fromJson(jsonStr, UpdateInfo::class.java)
            AtlasUpdater.update(info, patchFile)
            NLogger.i(TAG, "loadTPatch patch succeed")
            return true
        } catch (e: Exception) {
            NLogger.e(TAG, "loadTPatch ${e.message}")
        }

        return false
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

}