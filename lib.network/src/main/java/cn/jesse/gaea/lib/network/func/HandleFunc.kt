package cn.jesse.gaea.lib.network.func

import cn.jesse.gaea.lib.network.base.BaseResponse
import cn.jesse.gaea.lib.network.exception.ErrorDataCodeException
import io.reactivex.functions.Function

/**
 * 处理解析出来的 Base Response
 *
 * @author Jesse
 */
class HandleFunc<T> : Function<BaseResponse<T>, T> {

    override fun apply(response: BaseResponse<T>): T {
        if (!response.isSucceed()) {
            throw ErrorDataCodeException("${response.code} ${response.msg}")
        }

        return response.data!!
    }
}