package cn.jesse.gaea.lib.base.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.PermissionUtil
import cn.jesse.nativelogger.NLogger

/**
 * 基础Activity, 提供生命周期管理等
 *
 * @author Jesse
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var mTag = "BaseActivity"
    protected var permissionRequester: PermissionUtil.PermissionRequestObject? = null
    private var backPressedEnable = true
    private var fullScreenEnable = false

    /**
     * 设置是否需要回退页面
     */
    open fun setBackPressedEnable(): Boolean {
        return true
    }

    /**
     * 设置是否需要页面全屏
     */
    open fun setFullScreenEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenEnable = setFullScreenEnable()
        backPressedEnable = setBackPressedEnable()
        setFullScreen()
        mTag = getLogTag()
        NLogger.d(mTag, "onCreate")
        setContentView(getContentLayout())
        onActivityCreated()
    }

    override fun onStart() {
        super.onStart()
        NLogger.d(mTag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        NLogger.d(mTag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        NLogger.d(mTag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        NLogger.d(mTag, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        NLogger.d(mTag, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        NLogger.d(mTag, "onDestroy")
    }

    override fun onBackPressed() {
        if (!backPressedEnable) {
            return
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        NLogger.d(mTag, "onRequestPermissionsResult $requestCode $permissions $grantResults")
        if (CheckUtil.isNull(permissionRequester)) {
            return
        }

        permissionRequester!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setFullScreen() {
        if (!fullScreenEnable) {
            return
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取日志TAG
     */
    abstract fun getLogTag(): String

    /**
     * 获取要初始化的layout id
     */
    abstract fun getContentLayout(): Int

    /**
     * activity onCreate之后回调
     */
    abstract fun onActivityCreated()
}