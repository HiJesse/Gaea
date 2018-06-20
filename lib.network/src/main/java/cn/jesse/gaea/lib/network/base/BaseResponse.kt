package cn.jesse.gaea.lib.network.base

/**
 * response 基类, 定义最外层数据结构
 *
 * {
 *      code: 0,
 *      msg: "请求成功"
 *      data: {
 *          ...
 *      }
 * }
 *
 * @author Jesse
 */
class BaseResponse<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null
}