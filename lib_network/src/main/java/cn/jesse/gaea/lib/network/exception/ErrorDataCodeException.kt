package cn.jesse.gaea.lib.network.exception

/**
 * 业务code非0
 *
 * @author Jesse
 */
class ErrorDataCodeException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}