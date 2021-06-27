package com.kutugondrong.omovie.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kutugondrong.omovie.data.remote.Movie
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(tableName = "favorite_movie")
@Parcelize
data class FavoriteMovie(
    var id_movie: String,
    val original_title: String,
    val overview: String?,
    val poster_path: String
) : Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    val urlImage get() = "https://image.tmdb.org/t/p/w500/$poster_path"

    val movie get() = Movie(id_movie,overview, poster_path, original_title)
}