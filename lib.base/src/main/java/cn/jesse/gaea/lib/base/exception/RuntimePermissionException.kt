package cn.jesse.gaea.lib.base.exception

/**
 * 运行时权限异常
 *
 * @author Jesse
 */
class RuntimePermissionException  : RuntimeException {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}