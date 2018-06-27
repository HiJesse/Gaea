package cn.jesse.gaea.lib.common.bean

import java.io.Serializable

/**
 * 检查更新 bean
 *
 * @author Jesse
 */
class CheckUpdateBean : Serializable {
    // 宿主包名
    var hostName: String? = null
    // 宿主版本
    var hostVersion = 0

    // 宿主t patch
    var tPatch: PatchBean? = null

    // 远程bundle列表
    var bundles: List<RemoteBundleInfoBean>? = null
}