package cn.jesse.gaea

import android.content.Intent
import android.os.Bundle
import cn.jesse.gaea.lib.base.ui.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSplash.postDelayed({
            val intent = Intent()
            intent.setClassName(baseContext, "cn.jesse.gaea.plugin.main.ui.activity.MainActivity")
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.host_activity_in, R.anim.host_activity_out)
        }, 3000)
    }
}
