package cn.jesse.gaea.lib.network.transformer

import cn.jesse.gaea.lib.network.base.BaseResponse
import cn.jesse.gaea.lib.network.func.ErrorFunc
import cn.jesse.gaea.lib.network.func.HandleFunc
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * 转换原response 到业务数据. 并全局解析错误信息
 *
 * @author Jesse
 */
class ResponseTransformer<T> : ObservableTransformer<BaseResponse<T>, T> {

    override fun apply(upstream: Observable<BaseResponse<T>>): ObservableSource<T> {
        return upstream.map(HandleFunc()).onErrorResumeNext(ErrorFunc())
    }
}