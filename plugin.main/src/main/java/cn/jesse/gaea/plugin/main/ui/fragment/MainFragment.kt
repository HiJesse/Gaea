package cn.jesse.gaea.plugin.main.ui.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.lib.base.util.AppUtil
import cn.jesse.gaea.lib.base.util.AtlasRemoteUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.transactor.user.IToast
import cn.jesse.gaea.lib.common.util.AtlasUpdateUtil
import cn.jesse.gaea.lib.common.vm.UpdateViewModel
import cn.jesse.gaea.plugin.main.R
import cn.jesse.gaea.plugin.main.constant.PluginDef
import cn.jesse.nativelogger.NLogger
import kotlinx.android.synthetic.main.main_fragment_main.*

/**
 * 首页Fragment
 *
 * @author Jesse
 */
class MainFragment : BaseFragment() {
    private var loginStatus = false
    private lateinit var updateViewModel: UpdateViewModel

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.MainFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java)

        showLoginStatus()

        btnLogin.setOnClickListener {
            ActivityRouter.startActivity(this,
                    RemoteRouterDef.PluginUser.ACTIVITY_LOGIN,
                    RemoteRouterDef.PluginUser.RESULT_CODE_LOGIN_STATUS)
        }

        btnUninstallScanner.setOnClickListener {
            AtlasUpdateUtil.uninstallBundle(RemoteRouterDef.PluginScanner.BASE)
        }

        if (!"1.0.0".equals(AppUtil.getVersionName(ContextUtil.getApplicationContext()))) {
            btnLoadPatch.visibility = View.GONE
            return
        }

        btnLoadPatch.setOnClickListener {
            updateViewModel.checkUpdate(UpdateViewModel.Mode.PATCH)
        }

        btnPatternLock.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(RemoteRouterDef.LibCommon.PARAMS_LOCK_CLOSEABLE, true)
            ActivityRouter.startActivity(this,
                    RemoteRouterDef.LibCommon.ACTIVITY_PATTERN_LOCK,
                    bundle)
        }

        btnInvokeUser.setOnClickListener {
            AtlasRemoteUtil.fetchRemoteTransactor(activity!!, "", IToast::class.java, {iToast ->
                iToast.toast("从Main调起User的Toast")
            })
        }
    }

    /**
     * 显示用户登录状态
     */
    private fun showLoginStatus() {
        if (loginStatus) {
            tvLoginStatus.text = "(已登录)"
            tvLoginStatus.setTextColor(getResColor(R.color.common_text))
        } else {
            tvLoginStatus.text = "(未登录)"
            tvLoginStatus.setTextColor(getResColor(R.color.common_text_error))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RemoteRouterDef.PluginUser.RESULT_CODE_LOGIN_STATUS && resultCode == Activity.RESULT_OK) {
            loginStatus = data?.extras?.getBoolean(RemoteRouterDef.PluginUser.RESULT_LOGIN_STATUS)!!
            NLogger.d(mTag, "login status $loginStatus")
            showLoginStatus()
        }
    }

    companion object {
        fun newInstance(): BaseFragment {
            return MainFragment()
        }
    }
}