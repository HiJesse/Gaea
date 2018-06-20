package cn.jesse.gaea.lib.network.exception

/**
 * 使用HttpEngine 初始化相关异常
 *
 * @author Jesse
 */
class HttpEngineInitException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}