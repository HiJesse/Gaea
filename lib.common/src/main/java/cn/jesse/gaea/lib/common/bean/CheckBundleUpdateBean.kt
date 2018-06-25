package cn.jesse.gaea.lib.common.bean

import java.io.Serializable

/**
 * 检查更新 bean
 *
 * @author Jesse
 */
class CheckBundleUpdateBean : Serializable {
    // 宿主包名
    var hostName: String? = null
    // 宿主版本
    var hostVersion = 0
    // 宿主补丁url
    var hostPatchUrl: String? = null

    // 远程bundle列表
    var bundles: List<RemoteBundleInfoBean>? = null
}