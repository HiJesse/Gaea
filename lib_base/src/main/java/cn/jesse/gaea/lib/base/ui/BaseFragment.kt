package cn.jesse.gaea.lib.base.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import cn.jesse.nativelogger.NLogger

/**
 * 基础Fragment, 提供生命周期管理等
 *
 * @author Jesse
 */
abstract class BaseFragment : Fragment() {
    protected var mTag = "BaseFragment"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mTag = getLogTag()
        NLogger.d(mTag, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NLogger.d(mTag, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NLogger.d(mTag, "onViewCreated")
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

    override fun onDestroyView() {
        super.onDestroyView()
        NLogger.d(mTag, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        NLogger.d(mTag, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        NLogger.d(mTag, "onDetach")
    }

    /**
     * 根据R.color.id 获取颜色
     */
    open fun getResColor(id: Int): Int {
        return ContextCompat.getColor(activity!!.baseContext, id)
    }

    /**
     * 获取日志 TAG
     */
    abstract fun getLogTag(): String
}