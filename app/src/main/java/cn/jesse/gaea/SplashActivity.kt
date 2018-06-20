package cn.jesse.gaea

import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.common.ui.BaseActivity
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
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

    override fun onActivityCreated() {
        tvSplash.postDelayed({
            ActivityRouter.startActivity(this, RemoteRouterDef.PluginMain.ACTIVITY_MAIN)
            finish()
            overridePendingTransition(R.anim.host_activity_in, R.anim.host_activity_out)
        }, 3000)
    }
}
