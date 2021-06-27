package com.kutugondrong.omovie.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie(val id: String,
            val overview: String?,
            val poster_path: String,
            val original_title: String
) : Parcelable {

    val urlImage get() = "https://image.tmdb.org/t/p/w500/$poster_path"
    var isFavorite = false

    override fun equals(other: Any?): Boolean {
        return if (other is Movie) {
            val movie: Movie = other
            id == movie.id && original_title == movie.original_title && isFavorite == movie.isFavorite
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + poster_path.hashCode()
        result = 31 * result + original_title.hashCode()
        return result
    }
}