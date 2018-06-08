package cn.jesse.gaea

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.host_activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_activity_splash)

        tvSplash.postDelayed({
            val intent = Intent()
            intent.setClassName(baseContext, "cn.jesse.gaea.plugin.main.MainActivity")
            startActivity(intent)
            finish()
        }, 3000)
    }
}
