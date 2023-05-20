package com.android.watchify.data.api

import com.android.watchify.models.NewsResponse
import com.android.watchify.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ) : Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = "ua",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100
    ) : Response<NewsResponse>
}