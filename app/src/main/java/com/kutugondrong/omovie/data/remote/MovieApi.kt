package com.kutugondrong.omovie.data.remote

import com.kutugondrong.omovie.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    @GET("3/movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovie(@Query("page") position: Int): MovieResponse

    @GET("3/movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRateMovie(@Query("page") position: Int): MovieResponse

    @GET("3/movie/{movie_id}?api_key=$API_KEY")
    suspend fun getDetailMovie(@Path("movie_id") movieId: String): Response<DetailMovie>

}
