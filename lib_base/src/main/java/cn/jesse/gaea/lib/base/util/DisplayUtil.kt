package cn.jesse.gaea.lib.base.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


/**
 * dimension相关工具
 * 单位转换方法在强转int时都加了0.5f的偏移量, 目的是做出四舍五入的效果
 *
 * @author Jesse
 */
object DisplayUtil {

    var screenWidthPx = 0 //屏幕宽 px
    var screenHightPx = 0 //屏幕高 px
    var density: Float = 0.toFloat() //屏幕密度
    var densityDPI = 0 //屏幕密度
    var scaledDensity = 0 // 缩放
    var screenWidthDip = 0//  dp单位
    var screenHightDip = 0//  dp单位

    /**
     * 初始化display
     */
    fun init(context: Context) {
        val dm = context.resources.displayMetrics

        DisplayUtil.density = dm.density
        DisplayUtil.densityDPI = dm.densityDpi
        DisplayUtil.screenWidthPx = dm.widthPixels
        DisplayUtil.screenHightPx = dm.heightPixels
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(dm.widthPixels.toFloat())
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(dm.heightPixels.toFloat())
    }

    /**
     * 转化px为dp
     *
     * @param pxValue px值
     * @return dp值
     */
    fun px2dip(pxValue: Float): Int = (pxValue / density + 0.5f).toInt()

    /**
     * 转化dp为px
     *
     * @param dipValue dp值
     * @return px值
     */
    fun dip2px(dipValue: Float): Int = (dipValue * density + 0.5f).toInt()

    /**
     * 转化px为sp
     *
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(pxValue: Float): Int = (pxValue / scaledDensity + 0.5f).toInt()

    /**
     * 转换sp为px
     *
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(spValue: Float): Int = (spValue * scaledDensity + 0.5f).toInt()

    /**
     * 获取屏幕高度
     *
     * @param context context
     * @return px
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕宽度
     *
     * @param context context
     * @return pix
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }
}