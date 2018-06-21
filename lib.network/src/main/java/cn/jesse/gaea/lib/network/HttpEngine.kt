package cn.jesse.gaea.lib.network

import android.annotation.SuppressLint
import cn.jesse.gaea.lib.base.exception.UnsupportedOperationException
import cn.jesse.gaea.lib.base.util.CheckUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.network.base.BaseApiService
import cn.jesse.gaea.lib.network.base.BaseInterceptor
import cn.jesse.gaea.lib.network.exception.HttpEngineInitException
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * http 引擎, 提供网络接口相关服务
 *
 * @author Jesse
 */
class HttpEngine {
    private val context = ContextUtil.getApplicationContext()
    private val timeoutUnit = TimeUnit.MILLISECONDS
    private val defaultTimeout = 5000L

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: BaseApiService

    private lateinit var baseUrl: String
    private var connectionTimeout = defaultTimeout
    private var writeTimeout = defaultTimeout
    private var headers = emptyMap<String, String>()

    private var enable = false

    /**
     * 设置基础url
     */
    fun setBaseUrl(url: String): HttpEngine {
        baseUrl = url
        return this
    }

    /**
     * 设置连接超时和读写超时, 单位为毫秒
     */
    fun setTimeout(connectionTimeout: Long, writeTimeout: Long): HttpEngine {
        this.connectionTimeout = connectionTimeout
        this.writeTimeout = writeTimeout
        return this
    }

    /**
     * 设置http header
     */
    fun setHttpHeader(headers: Map<String, String>): HttpEngine {
        this.headers = headers
        return this
    }

    fun build() {
        if (CheckUtil.isNull(baseUrl)) {
            throw HttpEngineInitException("base url is null")
        }

        okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(connectionTimeout, timeoutUnit)
                .writeTimeout(writeTimeout, timeoutUnit)
                .addInterceptor(BaseInterceptor(headers))
                .build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        enable = true
        apiService = create(BaseApiService::class.java)
    }

    /**
     * 检查engine的状态, 异常情况下直接抛出异常
     */
    private fun checkStatus() {
        if (!enable) {
            throw throw HttpEngineInitException("u have to invoke build method first")
        }
    }

    /**
     * 根据class创建对应的API service
     */
    fun <T> create(service: Class<T>?): T {
        checkStatus()
        if (CheckUtil.isNull(service)) {
            throw UnsupportedOperationException("api service is null")
        }

        return retrofit.create(service)
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mInstance: HttpEngine? = null

        fun getInstance(): HttpEngine {
            if (mInstance == null) {
                synchronized(HttpEngine::class.java) {
                    if (mInstance == null) {
                        mInstance = HttpEngine()
                    }
                }
            }
            return mInstance!!
        }
    }
}