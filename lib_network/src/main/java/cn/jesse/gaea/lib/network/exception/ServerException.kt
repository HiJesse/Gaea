package cn.jesse.gaea.lib.network.exception

class ServerException : RuntimeException {
    var code  = 0

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}