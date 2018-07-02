package cn.jesse.gaea

import android.arch.lifecycle.ViewModelProviders
import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.base.ui.BaseActivity
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.vm.UpdateViewModel
import kotlinx.android.synthetic.main.host_activity_splash.*

/**
 * 闪屏欢迎页
 *
 * @author Jesse
 */
class SplashActivity : BaseActivity() {

    override fun getLogTag(): String {
        return "Host.SplashActivity"
    }

    override fun getContentLayout(): Int {
        return R.layout.host_activity_splash
    }

    override fun setFullScreenEnable(): Boolean {
        return true
    }

    override fun onActivityCreated() {
        val updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java)
        updateViewModel.checkUpdate(UpdateViewModel.Mode.BUNDLE)

        tvSplash.postDelayed({
            ActivityRouter.startActivity(this, RemoteRouterDef.PluginMain.ACTIVITY_MAIN)
            finish()
            overridePendingTransition(R.anim.host_activity_in, R.anim.host_activity_out)
        }, 3000)
    }
}
