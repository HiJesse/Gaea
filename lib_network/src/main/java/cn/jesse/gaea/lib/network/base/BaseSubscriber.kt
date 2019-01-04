package cn.jesse.gaea.lib.network.base

import android.content.Context
import android.widget.Toast
import cn.jesse.gaea.lib.base.util.NetworkUtil
import cn.jesse.gaea.lib.network.constant.NetworkError
import cn.jesse.gaea.lib.network.exception.ResponseException
import cn.jesse.nativelogger.NLogger
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * 基础订阅者
 *
 * @author Jesse
 */
abstract class BaseSubscriber<T>(context: Context) : Subscriber<T> {
    protected var TAG = "BaseSubscriber"
    private var context = context

    override fun onSubscribe(s: Subscription?) {
        NLogger.d(TAG, "onSubscribe")

        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show()
            onComplete()
        }
    }

    override fun onError(e: Throwable) {
        NLogger.e(TAG, e)

        if (e is ResponseException) {
            onError(e)
        } else {
            onError(ResponseException(NetworkError.UNKNOWN, e))
        }
    }

    override fun onComplete() {
        NLogger.d(TAG, "onCompleted")
    }

    abstract fun onError(e: ResponseException)
}