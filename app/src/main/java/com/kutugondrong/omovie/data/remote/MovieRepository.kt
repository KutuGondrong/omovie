package com.kutugondrong.omovie.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi) {
    fun getPopularMovie() =
        Pager(
            config = PagingConfig(
                pageSize = 100,
                initialLoadSize = 200,
                prefetchDistance = 60,
                maxSize = (100 + 60) * 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi, MoviePagingSource.POPULAR) }
        ).liveData

    fun getTopRateMovie() =
        Pager(
            config = PagingConfig(
                pageSize = 100,
                initialLoadSize = 200,
                prefetchDistance = 60,
                maxSize = (100 + 60) * 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi, MoviePagingSource.TOP_RATE) }
        ).liveData

    suspend fun getDetailMovie(movieId: String) = movieApi.getDetailMovie(movieId)
}