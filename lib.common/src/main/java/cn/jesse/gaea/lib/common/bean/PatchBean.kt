package cn.jesse.gaea.lib.common.bean

import java.io.Serializable

/**
 * t patch 更新信息
 *
 * @author Jesse
 */
class PatchBean : Serializable {
    // patch配置信息文件下载地址
    var patchConfigDownloadUrl: String? = null
    // patch文件下载地址
    var patchDownloadUrl: String? = null
    // patch文件MD5
    var patchFileMD5: String? = null
    // patch是否安装
    var patchInstalled = false
}