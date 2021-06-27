package com.kutugondrong.omovie.screen.detail

import androidx.lifecycle.*
import com.kutugondrong.omovie.data.local.FavoriteMovie
import com.kutugondrong.omovie.data.local.FavoriteMovieRepository
import com.kutugondrong.omovie.data.remote.DetailMovie
import com.kutugondrong.omovie.data.remote.Movie
import com.kutugondrong.omovie.data.remote.MovieRepository
import com.kutugondrong.omovie.data.remote.helper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor
    (
    private val repositoryMovie: MovieRepository,
    private val repositoryFavorite: FavoriteMovieRepository
) : ViewModel() {

    private val _detailMovie = MutableLiveData<Resource<DetailMovie>>()
    val detailMovie: LiveData<Resource<DetailMovie>>
        get() = _detailMovie

    fun getDetailMovie(movieId: String) {
        viewModelScope.launch {
            _detailMovie.postValue(Resource.loading(null))
            repositoryMovie.getDetailMovie(movieId).let {
                if (it.isSuccessful) {
                    _detailMovie.postValue(Resource.success(it.body()))
                } else _detailMovie.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

    fun addToFavorite(movie: DetailMovie){
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

    fun removeFromFavorite(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            repositoryFavorite.removeFromFavorite(id)
        }
    }

    suspend fun checkMovie(id: String) = repositoryFavorite.checkMovie(id)
}