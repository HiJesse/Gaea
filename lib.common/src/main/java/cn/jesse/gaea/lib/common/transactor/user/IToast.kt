package cn.jesse.gaea.lib.common.transactor.user

/**
 * 测试跨模块方法调起 (非业务, 测试)
 *
 * @author Jesse
 */
interface IToast {
    /**
     * 测试跨bundle toast
     */
    fun toast(msg: String)
}