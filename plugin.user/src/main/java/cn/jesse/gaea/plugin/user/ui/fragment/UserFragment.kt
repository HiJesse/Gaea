package cn.jesse.gaea.plugin.user.ui.fragment

import android.os.Bundle
import android.taobao.atlas.remote.IRemote
import android.taobao.atlas.remote.IRemoteTransactor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.plugin.user.R
import kotlinx.android.synthetic.main.user_fragment_user.*

/**
 * 用户中心Fragment
 *
 * @author Jesse
 */
class UserFragment : BaseFragment(), IRemote {

    override fun getLogTag(): String {
        return "User.UserFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            ActivityRouter.startActivity(this, RemoteRouterDef.PluginUser.ACTIVITY_LOGIN)
        }
    }

    override fun call(commandName: String?, args: Bundle?, callback: IRemoteTransactor.IResponse?): Bundle {
        return null as Bundle
    }

    override fun <T : Any?> getRemoteInterface(interfaceClass: Class<T>?, args: Bundle?): T? {
        return null
    }

    companion object {
        fun newInstance(): BaseFragment {
            return UserFragment()
        }
    }
}