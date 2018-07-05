package cn.jesse.gaea.plugin.user.transactor

import android.os.Bundle
import android.taobao.atlas.remote.IRemote
import android.taobao.atlas.remote.IRemoteTransactor
import cn.jesse.gaea.lib.common.transactor.user.IBundleInvoke

/**
 * User模块 跨组件调用处理中心
 *
 * @author Jesse
 */
class UserTransactor : IRemote {

    override fun call(commandName: String?, args: Bundle?, callback: IRemoteTransactor.IResponse?): Bundle {
        return Bundle(args)
    }

    override fun <T : Any?> getRemoteInterface(interfaceClass: Class<T>?, args: Bundle?): T {
        return when (interfaceClass) {
            IBundleInvoke::class.java -> BundleInvoke() as T
            else -> null as T
        }
    }
}