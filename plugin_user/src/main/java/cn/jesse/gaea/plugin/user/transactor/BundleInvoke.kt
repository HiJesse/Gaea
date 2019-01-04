package cn.jesse.gaea.plugin.user.transactor

import android.content.Context
import cn.jesse.gaea.lib.common.transactor.user.IBundleInvoke
import es.dmoral.toasty.Toasty

/**
 * 测试跨组件通讯
 *
 * @author Jesse
 */
class BundleInvoke : IBundleInvoke {
    override fun toast(context: Context, msg: String) {
        Toasty.normal(context, msg).show()
    }
}