package cn.jesse.gaea.lib.base.livedata

/**
 * live data 观察数据结构扩展, 支持数据, 数据状态
 *
 * @author Jesse
 */
open class DataStatusResult<T> {
    var data: T? = null
    var message: String? = null
    var succeed = false

    constructor(resultStatus: Boolean) {
        data = null
        succeed = resultStatus
    }

    constructor(resultStatus: Boolean, resultMessage: String?) {
        data = null
        succeed = resultStatus
        message = resultMessage
    }

    constructor(resultData: T?) : this(true, null) {
        data = resultData
    }

    constructor(resultData: T?, resultStatus: Boolean) {
        data = resultData
        succeed = resultStatus
    }
}