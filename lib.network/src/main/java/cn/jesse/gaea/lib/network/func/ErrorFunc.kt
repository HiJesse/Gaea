package cn.jesse.gaea.lib.network.func

import cn.jesse.gaea.lib.network.exception.ExceptionHandle
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * 处理全局错误
 *
 * @author Jesse
 */
class ErrorFunc<T> : Function<Throwable, Observable<T>> {

    override fun apply(t: Throwable): Observable<T> {
        return Observable.error<T>(ExceptionHandle.handleException(t))
    }
}