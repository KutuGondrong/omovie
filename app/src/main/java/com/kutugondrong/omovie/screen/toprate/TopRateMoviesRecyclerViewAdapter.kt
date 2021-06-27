package com.kutugondrong.omovie.screen.toprate

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.data.remote.Movie
import com.kutugondrong.omovie.databinding.FragmentTopRateMoviesBinding
import com.kutugondrong.omovie.screen.main.MainFragmentDirections

class TopRateMoviesRecyclerViewAdapter(
    private val checkIsFavorite: (movieId: String, position: Int) -> Unit,
    private val updateMovie: (position: Int, isFavorite: Boolean) -> Unit
) :
    PagingDataAdapter<Movie, TopRateMoviesRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(private val binding: FragmentTopRateMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, position: Int) {
            with(binding) {
                Glide.with(itemView)
                    .load(movie.urlImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_launcher_foreground)
                    )
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgMovie)
                txtTitle.text = movie.original_title
                txtSubDesc.text = movie.overview

                btnLike.isChecked = movie.isFavorite
                checkIsFavorite.invoke(movie.id, position)
                btnLike.setOnClickListener {
                    updateMovie.invoke(position, !btnLike.isChecked)
                }

                itemView.setOnClickListener {
                    val direction =
                        MainFragmentDirections.actionMainFragmentToDetailMovieFragment(movie)
                    itemView.findNavController().navigate(direction)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val moviesBinding = FragmentTopRateMoviesBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(moviesBinding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

        }
    }

    fun getItemByPosition(position: Int): Movie? {
        return getItem(position)
    }
}