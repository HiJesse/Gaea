package cn.jesse.gaea.lib.base.livedata

/**
 * live data 观察数据结构扩展. 支持数据, 数据状态, 填充扩展信息
 *
 * @author Jesse
 */
open class DataStatusExtendResult<T, T1> : DataStatusResult<T> {
    var extend: T1? = null

    constructor(resultExtend: T1?) : super(false) {
        extend = resultExtend
    }

    constructor(resultData: T?, resultStatus: Boolean, resultExtend: T1?) : super(resultData, resultStatus) {
        extend = resultExtend
    }
}