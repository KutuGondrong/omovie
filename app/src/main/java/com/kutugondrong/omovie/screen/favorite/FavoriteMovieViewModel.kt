package com.kutugondrong.omovie.screen.favorite

import androidx.lifecycle.ViewModel
import com.kutugondrong.omovie.data.local.FavoriteMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    repository: FavoriteMovieRepository,
    private val repositoryFavorite: FavoriteMovieRepository
) : ViewModel() {
    val movies = repository.getFavoriteMovies()

    fun removeFromFavorite(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryFavorite.removeFromFavorite(id)
        }
    }
}