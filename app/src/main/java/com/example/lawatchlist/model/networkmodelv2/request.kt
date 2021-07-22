package com.example.lawatchlist.model.networkmodelv2


import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIResponse(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val movieTMDB: List<MovieTMDB>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)

fun APIResponse.toDomainModel() : List<MovieTMDB> = this.movieTMDB


@JsonClass(generateAdapter = true)
data class MovieTMDB(
    @Json(name = "adult")
    val adult: Boolean,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @Json(name = "id")
    val id: Int,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_title")
    val originalTitle: String,
    @Json(name = "overview")
    val overview: String?,
    @Json(name = "popularity")
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "runtime")
    val runtime: Int?,
    @Json(name = "title")
    val title: String,
    @Json(name = "video")
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
) {

}