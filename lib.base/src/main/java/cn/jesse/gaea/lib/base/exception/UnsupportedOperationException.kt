package cn.jesse.gaea.lib.base.exception

/**
 * 非法操作运行时异常
 *
 * @author Jesse
 */

class UnsupportedOperationException : RuntimeException {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}
