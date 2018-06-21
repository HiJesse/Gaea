package cn.jesse.gaea.lib.network.base

import io.reactivex.Observable
import retrofit2.http.QueryMap
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 基础 API service
 *
 * @author Jesse
 */
interface BaseApiService {

    @GET("{url}")
    fun executeGet(
            @Path("url") url: String,
            @QueryMap maps: Map<String, String>
    ): Observable<BaseResponse<Any>>


    @POST("{url}")
    fun executePost(
            @Path("url") url: String,
            @QueryMap maps: Map<String, String>
    ): Observable<ResponseBody>
}