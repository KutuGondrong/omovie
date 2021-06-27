package com.kutugondrong.omovie.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

private const val PAGE_INDEX_START = 1
private const val INCREASE = 1

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val type: String
) : PagingSource<Int, Movie>() {

    companion object {
        const val POPULAR = "popular"
        const val TOP_RATE = "top_rate"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: PAGE_INDEX_START

            val response = when (type) {
                POPULAR -> {
                    movieApi.getPopularMovie(position)
                }
                TOP_RATE -> {
                    movieApi.getTopRateMovie(position)
                }
                else -> {
                    movieApi.getPopularMovie(position)
                }
            }
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position == PAGE_INDEX_START) null else position - INCREASE,
                nextKey = if (movies.isEmpty()) null else position + INCREASE
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

}