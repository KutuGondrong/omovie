package com.kutugondrong.omovie.screen.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import androidx.fragment.app.viewModels
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.databinding.FragmentFavoriteMoviesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies_list) {

    private val viewModel by viewModels<FavoriteMovieViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoriteMoviesListBinding.bind(view)

        val adapterFavorite = FavoriteMoviesRecyclerViewAdapter {
            viewModel.removeFromFavorite(it)
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            adapterFavorite.setMovieList(it)
            binding.apply {
                with(listFavoriteMovie) {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = adapterFavorite
                }
            }
        }
    }

}