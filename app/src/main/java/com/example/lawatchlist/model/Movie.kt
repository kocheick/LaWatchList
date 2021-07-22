package com.example.lawatchlist.model

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

//@Entity(tableName = "movie_table")
//@JsonClass(generateAdapter = true)
//@Parcelize
//data class Movie(
//    @field:Json(name = "id") @PrimaryKey var id: Int? = null,
//    @field:Json(name = "name") @ColumnInfo(name = "title") val title: String,
//    @field:Json(name = "summary") @ColumnInfo(name = "summary") var summary: String?="N/A",
//    @field:Json(name = "premiered") @ColumnInfo(name = "release_date") val releaseDate: String?,
//    @field:Json(name = "runtime") @ColumnInfo(name = "duration") val duration: Int? = "N/A".toIntOrNull(),
//    @ColumnInfo(name = "isFavorite") @Transient var isFavorite: Boolean = false,
//    @field:Json(name = "image")@ColumnInfo(name = "url") val imageUrl: Image?
//) : Parcelable {
//    init {
//        summary?.let {
//            summary = fromHtml(summary, Html.FROM_HTML_MODE_LEGACY).toString()
//        }
//    }
//}

@Parcelize
data class Movie(
    val id: Int?, val title: String, val summary: String?, val releaseDate: String?, val isFavorite: Boolean, val images: Image?, val isTrending: Boolean, val isTopRated: Boolean,
    val isSearchItem: Boolean, val genreIds:List<Int>
) : Parcelable

@Entity(tableName = "movie_table")
data class MovieDBModel(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "summary") val summary: String? = "N/A",
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "genres_id") val genreIds: List<Int>,
    @ColumnInfo(name = "url") val imageUrl: Image?,
    @ColumnInfo(name = "addedAt") val addedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "isTopRated") val isTopRated: Boolean,
    @ColumnInfo(name = "isTrending") val isTrending: Boolean,
    @ColumnInfo(name = "isSearchItem") val isSearchItem: Boolean,
    @ColumnInfo(name = "isFavorite") @Transient var isFavorite: Boolean = false,

    )



@JsonClass(generateAdapter = true)
@Parcelize
data class Image(
    var posterPath: String?, // http://static.tvmaze.com/uploads/images/medium_portrait/76/191071.jpg
    var backdropPath: String? // http://static.tvmaze.com/uploads/images/original_untouched/76/191071.jpg
) : Parcelable {
    init {
        if (posterPath != null) {
            if (backdropPath == null) backdropPath = posterPath
        } else {
            posterPath = backdropPath
        }

    }
}


fun MovieDBModel.toDomain(): Movie = Movie(
    id = id, title = title, summary = summary,
    releaseDate = releaseDate, isFavorite = isFavorite, images = imageUrl,isTrending, isTopRated, isSearchItem,genreIds
)
