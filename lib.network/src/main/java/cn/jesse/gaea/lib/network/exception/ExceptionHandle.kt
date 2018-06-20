package cn.jesse.gaea.lib.network.exception

import android.net.ParseException
import cn.jesse.gaea.lib.network.constant.NetworkError
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * 异常处理
 *
 * @author Jesse
 */
object ExceptionHandle {

    private val UNAUTHORIZED = 401
    private val FORBIDDEN = 403
    private val NOT_FOUND = 404
    private val REQUEST_TIMEOUT = 408
    private val INTERNAL_SERVER_ERROR = 500
    private val BAD_GATEWAY = 502
    private val SERVICE_UNAVAILABLE = 503
    private val GATEWAY_TIMEOUT = 504

    /**
     * 根据不同的异常信息, 二次包装分类
     */
    fun handleException(e: Throwable): ResponseException {
        val ex: ResponseException

        if (e is HttpException) {
            ex = when (e.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND,
                REQUEST_TIMEOUT, GATEWAY_TIMEOUT,
                INTERNAL_SERVER_ERROR, BAD_GATEWAY,
                SERVICE_UNAVAILABLE -> ResponseException(NetworkError.HTTP_ERROR, "网络错误", e)
                else -> ResponseException(NetworkError.HTTP_ERROR, "网络错误", e)
            }
        } else if (e is ServerException) {
            ex = ResponseException(e.code, e.message!!, e)
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            ex = ResponseException(NetworkError.PARSE_ERROR, "解析错误", e)
        } else if (e is ConnectException) {
            ex = ResponseException(NetworkError.NETWORD_ERROR, "连接失败", e)
        } else if (e is javax.net.ssl.SSLHandshakeException) {
            ex = ResponseException(NetworkError.SSL_ERROR, "证书验证失败", e)
        } else if (e is ConnectTimeoutException) {
            ex = ResponseException(NetworkError.TIMEOUT_ERROR, "连接超时", e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = ResponseException(NetworkError.TIMEOUT_ERROR, "连接超时", e)
        } else {
            ex = ResponseException(NetworkError.UNKNOWN, "未知错误", e)
        }
        return ex
    }
}