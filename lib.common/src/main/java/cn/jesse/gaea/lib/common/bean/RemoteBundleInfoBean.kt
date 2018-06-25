package cn.jesse.gaea.lib.common.bean

import java.io.Serializable

/**
 * 远程bundle信息
 *
 * @author Jesse
 */
class RemoteBundleInfoBean : Serializable {
    // bundle 名称
    var bundleName: String? = null
    // bundle 版本
    var bundleVersion: String? = null
    // bundle下载地址
    var bundleDownloadUrl: String? = null
    // bundle文件MD5
    var bundleFileMD5: String? = null
}