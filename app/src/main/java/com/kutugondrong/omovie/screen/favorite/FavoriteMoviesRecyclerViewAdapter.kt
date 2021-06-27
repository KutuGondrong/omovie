package com.kutugondrong.omovie.screen.favorite

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.data.local.FavoriteMovie
import com.kutugondrong.omovie.databinding.FragmentFavoriteMoviesBinding
import com.kutugondrong.omovie.screen.main.MainFragmentDirections

class FavoriteMoviesRecyclerViewAdapter(
    private val removeFavoriteMovie: (id: String) -> Unit
) : RecyclerView.Adapter<FavoriteMoviesRecyclerViewAdapter.ViewHolder>() {

    private lateinit var listMovie: List<FavoriteMovie>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentFavoriteMoviesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun setMovieList(movies: List<FavoriteMovie>) {
        this.listMovie = movies
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = listMovie.size

    inner class ViewHolder(private val binding: FragmentFavoriteMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val favoriteMovie = listMovie[position]
            with(binding) {
                Glide.with(itemView)
                    .load(favoriteMovie.urlImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_launcher_foreground)
                    )
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgMovie)

                txtTitle.text = favoriteMovie.original_title
                txtSubDesc.text = favoriteMovie.overview
                itemView.setOnClickListener {
                    val direction =
                        MainFragmentDirections.actionMainFragmentToDetailMovieFragment(favoriteMovie.movie)
                    itemView.findNavController().navigate(direction)
                }
                btnLike.isChecked = true
                btnLike.setOnClickListener {
                    notifyItemRemoved(position).also {
                        removeFavoriteMovie.invoke(favoriteMovie.id_movie)
                    }
                }
            }
        }
    }
}