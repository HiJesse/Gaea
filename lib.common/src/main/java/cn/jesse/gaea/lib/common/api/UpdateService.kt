package cn.jesse.gaea.lib.common.api

import cn.jesse.gaea.lib.common.bean.CheckBundleUpdateBean
import cn.jesse.gaea.lib.network.base.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * 升级 相关API Service
 *
 * @author Jesse
 */
interface UpdateService {

    @GET("/gaea/update/checkBundle")
    fun checkBundle(): Observable<BaseResponse<CheckBundleUpdateBean>>
}