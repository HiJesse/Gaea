package cn.jesse.gaea.lib.common.bean

/**
 * Pattern Lock 的各种状态
 *
 * @author Jesse
 */
enum class PatternLockStatus {
    // 未设置
    INIT_UNSET,
    // 已设置
    INIT_SETTED,
    // 未设置下, 第一次设置成功
    SUCCEED_FIRST,
    // 未设置下, 第二次设置成功
    SUCCEED_SECOND,
    // 解锁成功
    SUCCEED_UNLOCK,
    // 未设置下, pattern校验不匹配
    ERROR_DOUBLE_CHECK,
    // 解锁失败
    ERROR_UNLOCK,
    // 手势不合法
    ERROR_PATTERN
}