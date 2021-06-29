package com.kutugondrong.omovie.screen.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.data.remote.helper.Status
import com.kutugondrong.omovie.databinding.FragmentDetailMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private val args by navArgs<DetailMovieFragmentArgs>()

    val viewModel by viewModels<DetailMovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailMovieBinding.bind(view)

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.inflateMenu(R.menu.menu)
            toolbar.setOnMenuItemClickListener {
                onOptionsItemSelected(it)
            }
            viewModel.getDetailMovie(args.movie.id)
            viewModel.detailMovie.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            Glide.with(view.context)
                                .load(data.urlImageBackdrop)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                )
                                .error(R.drawable.ic_launcher_foreground)
                                .into(appBarImage)
                            Glide.with(view.context)
                                .load(data.urlImagePoster)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                )
                                .error(R.drawable.ic_launcher_foreground)
                                .into(imageView)
                            txtTitleDetail.text = data.original_title
                            txtVote.text = getString(R.string.votes, data.vote_count)
                            txtRating.text = getString(R.string.rating, data.vote_average)
                            txtContent.text = data.overview
                            txtTrailerContent.text = data.overview
                            txtDate.text = data.release_date
                            Glide.with(view.context)
                                .load(data.urlImagePoster)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.ic_launcher_foreground)
                                .into(appBarImage)

                            var isChecked = false

                            CoroutineScope(Dispatchers.IO).launch{
                                val count = viewModel.checkMovie(data.id)
                                withContext(Main){
                                    if (count > 0){
                                        btnLike.isChecked = true
                                        isChecked = true
                                    }else{
                                        btnLike.isChecked = false
                                        isChecked = false
                                    }
                                }
                            }

                            btnLike.setOnClickListener {
                                isChecked = !isChecked
                                if (isChecked){
                                    viewModel.addToFavorite(data)
                                } else{
                                    viewModel.removeFromFavorite(data.id)
                                }
                                btnLike.isChecked = isChecked
                            }
                        }
                        childState.visibility = View.VISIBLE
                        parentState.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        childState.visibility = View.GONE
                        parentState.visibility = View.VISIBLE
                        progressLoading.visibility = View.VISIBLE
                        txtError.visibility = View.GONE
                        btnRetry.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        childState.visibility = View.GONE
                        parentState.visibility = View.VISIBLE
                        progressLoading.visibility = View.GONE
                        txtError.visibility = View.VISIBLE
                        btnRetry.visibility = View.VISIBLE
                    }
                }
            })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {

            }
            R.id.notification -> {

            }
        }
        return true
    }
}