package cn.jesse.gaea.lib.network.exception

/**
 * response 相关错误
 *
 * @author Jesse
 */
class ResponseException : Exception {
    var code = 0

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(code: Int, cause: Throwable) : super(cause) {
        this.code = code
    }

    constructor(code: Int, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }
}