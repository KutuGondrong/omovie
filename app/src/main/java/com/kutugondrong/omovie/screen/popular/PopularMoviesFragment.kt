package com.kutugondrong.omovie.screen.popular

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.databinding.FragmentPopularMoviesListBinding
import com.kutugondrong.omovie.screen.ViewLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMoviesFragment : Fragment(R.layout.fragment_popular_movies_list) {

    val viewModel by viewModels<PopularMovieViewModel>()

    private val adapter = PopularMoviesRecyclerViewAdapter(
        { movieId, btnLike ->
            checkIsFavorite(movieId, btnLike)
        },
        { position, isFavorite ->
            updateMovie(position, isFavorite)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPopularMoviesListBinding.bind(view)

        binding.apply {
            val headerAdapter = ViewLoadStateAdapter {

            }
            val footerAdapter = ViewLoadStateAdapter { adapter.retry() }
            val concatAdapter = adapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
            // list is the RecyclerView
            listPopularMovie.adapter = concatAdapter
            val layoutManager = GridLayoutManager(context, 2)
            listPopularMovie.layoutManager = layoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 && headerAdapter.itemCount > 0) {
                        2
                    } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
                }
            }
            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressLoading.isVisible = loadState.source.refresh is LoadState.Loading
                listPopularMovie.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtError.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


    }

    private fun checkIsFavorite(movieId: String, position: Int) {
        viewModel.checkMovie(movieId).observe(viewLifecycleOwner) {
            adapter.getItemByPosition(position)?.apply {
                if (isFavorite != (it > 0)) {
                    isFavorite = (it > 0)
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun updateMovie(position: Int, _isFavorite: Boolean) {
        adapter.getItemByPosition(position)?.apply {
            isFavorite = _isFavorite
        }.also {
            it?.let {
                if (it.isFavorite) {
                    viewModel.removeFromFavorite(it.id)
                } else {
                    viewModel.addToFavorite(it)
                }
            }
            adapter.notifyItemChanged(position)
        }
    }
}