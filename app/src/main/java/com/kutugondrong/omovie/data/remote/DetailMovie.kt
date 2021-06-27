package com.kutugondrong.omovie.data.remote

class DetailMovie(val id: String,
                  val overview: String?,
                  val poster_path: String,
                  private val backdrop_path: String,
                  val original_title: String,
                  val vote_average: String,
                  val vote_count: String,
                  val release_date: String
) {

    val urlImagePoster get() = "https://image.tmdb.org/t/p/w500/$poster_path"

    val urlImageBackdrop get() = "https://image.tmdb.org/t/p/w500/$backdrop_path"

}