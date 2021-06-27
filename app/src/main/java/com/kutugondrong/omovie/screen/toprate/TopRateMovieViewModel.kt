package com.kutugondrong.omovie.screen.toprate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.kutugondrong.omovie.data.local.FavoriteMovie
import com.kutugondrong.omovie.data.local.FavoriteMovieRepository
import com.kutugondrong.omovie.data.remote.Movie
import com.kutugondrong.omovie.data.remote.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopRateMovieViewModel @Inject constructor(
    repository: MovieRepository,
    private val repositoryFavorite: FavoriteMovieRepository
) : ViewModel() {

    val movies: LiveData<PagingData<Movie>> = repository.getTopRateMovie()

    fun addToFavorite(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryFavorite.addToFavorite(
                FavoriteMovie(
                    movie.id,
                    movie.original_title,
                    movie.overview,
                    movie.poster_path
                )
            )
        }
    }

    fun removeFromFavorite(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryFavorite.removeFromFavorite(id)
        }
    }

    fun checkMovie(id: String) = repositoryFavorite.checkMovieLive(id)
}