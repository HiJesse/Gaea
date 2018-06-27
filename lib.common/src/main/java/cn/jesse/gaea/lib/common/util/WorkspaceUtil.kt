package cn.jesse.gaea.lib.common.util

import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import java.io.File

/**
 * 工作空间相关工具
 *
 * @author Jesse
 */
class WorkspaceUtil {
    // 根目录
    lateinit var root: File
        private set
    // 日志目录
    lateinit var rootLogs: File
        private set
    // 图片目录
    lateinit var rootImages: File
        private set
    // 文件目录
    lateinit var rootFiles: File
        private set
    // 文件目录-bundle
    lateinit var bundleFiles: File
        private set
    // 文件目录-patch
    lateinit var patchFiles: File
        private set

    private val DIR_IMAGES = "rootImages"
    private val DIR_LOGS = "rootLogs"
    private val DIR_FILES = "rootFiles"

    private val DIR_SUB_BUNDLES = "bundles"
    private val DIR_SUB_PATCHES = "patches"

    init {
        initWorkspace()
    }

    /**
     * 清除工作空间
     */
    fun clear() {
        if (!root.exists()) {
            return
        }
        root.delete()
        initWorkspace()
    }

    /**
     * 初始化工作空间
     */
    private fun initWorkspace() {
        root = when (CheckUtil.isNotNull(ContextUtil.getApplicationContext().externalCacheDir)) {
            true -> ContextUtil.getApplicationContext().externalCacheDir
            else -> ContextUtil.getApplicationContext().cacheDir
        }

        rootLogs = makeDir(root, DIR_LOGS)
        rootImages = makeDir(root, DIR_IMAGES)
        rootFiles = makeDir(root, DIR_FILES)

        bundleFiles = makeDir(rootFiles, DIR_SUB_BUNDLES)
        patchFiles = makeDir(rootFiles, DIR_SUB_PATCHES)
    }

    private fun makeDir(root: File, child: String): File {
        val target = File(root, child)
        if (!target.exists()) {
            target.mkdirs()
        }
        return target
    }


    companion object {
        @Volatile
        private var mInstance: WorkspaceUtil? = null

        fun getInstance(): WorkspaceUtil {
            if (mInstance == null) {
                synchronized(WorkspaceUtil::class.java) {
                    if (mInstance == null) {
                        mInstance = WorkspaceUtil()
                    }
                }
            }
            return mInstance!!
        }
    }

}