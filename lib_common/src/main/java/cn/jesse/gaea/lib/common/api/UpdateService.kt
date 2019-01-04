package cn.jesse.gaea.lib.common.api

import cn.jesse.gaea.lib.common.bean.CheckUpdateBean
import cn.jesse.gaea.lib.network.base.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * 升级 相关API Service
 *
 * @author Jesse
 */
interface UpdateService {

    @GET("/gaea/update/checkUpdate")
    fun checkUpdate(): Observable<BaseResponse<CheckUpdateBean>>
}