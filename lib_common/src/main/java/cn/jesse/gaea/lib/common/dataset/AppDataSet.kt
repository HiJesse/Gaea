package cn.jesse.gaea.lib.common.dataset

import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.common.bean.PatchBean
import cn.jesse.gaea.lib.common.bean.RemoteBundleInfoBean
import cn.jesse.gaea.lib.common.constant.SPDef
import cn.jesse.gaea.lib.common.util.SPUtil
import cn.jesse.nativelogger.NLogger
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

/**
 * 应用信息数据集
 *
 * @author Jesse
 */
class AppDataSet {
    private val TAG = "AppDataSet"
    private val appSP = SPUtil.appSP


    // 手势解锁MD5
    var patternLock: String? = null
        get() {
            if (CheckUtil.isNull(field)) {
                field = appSP.getStringFlag(SPDef.App.KEY_PATTERN_LOCK, "")
            }
            return field
        }
        set(value) {
            if (field == value) {
                return
            }
            field = value
            appSP.setFlag(SPDef.App.KEY_PATTERN_LOCK, field)
        }

    // 宿主版本
    var hostVersion: Int = 0
        get() {
            if (CheckUtil.isNull(field)) {
                field = appSP.getIntFlag(SPDef.App.KEY_HOST_VERSION, 0)
            }
            return field
        }
        set(value) {
            if (field == value) {
                return
            }
            field = value
            appSP.setFlag(SPDef.App.KEY_HOST_VERSION, field)
        }

    // t patch 信息
    var patchInfo: PatchBean? = null
        get() {
            if (CheckUtil.isNotNull(field)) {
                return field
            }

            field = appSP.getJsonObjectFlag(SPDef.App.KEY_PATCH_INFO, PatchBean::class.java)

            return field
        }
        set(value) {
            field = value
            appSP.setJsonFlag(SPDef.App.KEY_PATCH_INFO, field)
        }

    // 远程bundles 信息
    var bundlesInfo: List<RemoteBundleInfoBean>? = null
        get() {
            if (CheckUtil.isNotNull(field)) {
                return field
            }

            val json = appSP.getStringFlag(SPDef.App.KEY_BUNDLES_INFO)

            try {
                val jsonArray = JsonParser().parse(json).asJsonArray
                val gson = Gson()
                val fieldArray = mutableListOf<RemoteBundleInfoBean>()

                for (element in jsonArray) {
                    fieldArray.add(gson.fromJson(element, RemoteBundleInfoBean::class.java))
                }

                field = fieldArray
            } catch (e: JsonParseException) {
                NLogger.e(TAG, "getBundlesInfo ${e.message}")
            } catch (e: JsonSyntaxException) {
                NLogger.e(TAG, "getBundlesInfo ${e.message}")
            }

            return field
        }
        set(value) {
            field = value
            appSP.setJsonFlag(SPDef.App.KEY_BUNDLES_INFO, field)
        }

    /**
     * 根据bundle名称从bundle列表中获取特定的bundle信息
     *
     * @param bundleName bundle名称
     */
    fun getBundleInfo(bundleName: String): RemoteBundleInfoBean? {
        if (CheckUtil.isNull(bundlesInfo) || bundlesInfo!!.isEmpty()) {
            return null
        }

        var bundleInfo: RemoteBundleInfoBean? = null

        for (item in bundlesInfo!!) {
            if (bundleName.equals(item.bundleName)) {
                bundleInfo = item
            }
        }

        return bundleInfo
    }
}