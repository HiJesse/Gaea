package cn.jesse.gaea.plugin.user.api

import retrofit2.http.GET
import cn.jesse.gaea.lib.network.base.BaseResponse
import io.reactivex.Observable

/**
 * 用户模块 API service
 *
 * @author Jesse
 */
interface UserService {

    @GET("/gaea/user/login")
    fun login(): Observable<BaseResponse<LoginBean>>
}