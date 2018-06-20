package cn.jesse.gaea.lib.network.base

import cn.jesse.gaea.lib.base.util.CheckUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * interceptor基类, 提供基础的header转移
 *
 * @author Jesse
 */
class BaseInterceptor(private val headers: Map<String, String>?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()

        if (CheckUtil.isNull(headers) || headers!!.isEmpty()) {
            return chain.proceed(builder.build())
        }

        val keys = headers.keys
        for (headerKey in keys) {
            builder.addHeader(headerKey, headers[headerKey]).build()
        }

        return chain.proceed(builder.build())

    }
}