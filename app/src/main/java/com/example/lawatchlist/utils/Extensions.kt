package com.example.lawatchlist

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawatchlist.model.*
import com.example.lawatchlist.model.networkmodelv2.MovieTMDB
import com.example.lawatchlist.model.networkmodelv2.genre.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun List<MovieDBModel>.toUIModel(): List<Movie> = map {
    Movie(
        id = it.id,
        title = it.title,
        summary = it.summary,
        releaseDate = it.releaseDate,
        isFavorite = it.isFavorite,
        images = it.imageUrl,
        isTrending = it.isTrending,
        isTopRated = it.isTopRated,
        isSearchItem = it.isSearchItem,genreIds = it.genreIds
    )
}

fun Movie.toDBModel(): MovieDBModel = MovieDBModel(
    id = id,
    title = title,
    summary = summary,
    releaseDate = releaseDate,
    isFavorite = isFavorite,
    imageUrl = images,
    isSearchItem = isSearchItem,
    isTopRated = isTopRated,
    isTrending = isTrending,
    genreIds = genreIds)

//fun List<MovieAPIModel>.toSearchDBModel(): List<MovieSearchResult> = map { MovieSearchResult(
//    id = it.id,
//    title = it.title,
//    summary = it.summary,
//    releaseDate = it.releaseDate,
//    duration = it.duration,
//    isFavorite = false,
//    imageUrl = it.imageUrl
//)
//}

fun List<MovieTMDB>.toDBModel(): List<MovieDBModel> = map {
    MovieDBModel(
        id = it.id,
        title = it.title,
        summary = it.overview,
        releaseDate = it.releaseDate,
        isFavorite = false,
        imageUrl = Image(it.posterPath, it.backdropPath),
        isSearchItem = false,
        isTopRated = false,
        isTrending = false,
        genreIds = it.genreIds
    )
}


var a = """ [
    {
        "id": 28,
        "name": "Action"
    },
    {
        "id": 12,
        "name": "Adventure"
    },
    {
        "id": 16,
        "name": "Animation"
    },
    {
        "id": 35,
        "name": "Comedy"
    },
    {
        "id": 80,
        "name": "Crime"
    },
    {
        "id": 99,
        "name": "Documentary"
    },
    {
        "id": 18,
        "name": "Drama"
    },
    {
        "id": 10751,
        "name": "Family"
    },
    {
        "id": 14,
        "name": "Fantasy"
    },
    {
        "id": 36,
        "name": "History"
    },
    {
        "id": 27,
        "name": "Horror"
    },
    {
        "id": 10402,
        "name": "Music"
    },
    {
        "id": 9648,
        "name": "Mystery"
    },
    {
        "id": 10749,
        "name": "Romance"
    },
    {
        "id": 878,
        "name": "Science Fiction"
    },
    {
        "id": 10770,
        "name": "TV Movie"
    },
    {
        "id": 53,
        "name": "Thriller"
    },
    {
        "id": 10752,
        "name": "War"
    },
    {
        "id": 37,
        "name": "Western"
    }]"""

var collectionType: Type = object : TypeToken<Collection<Genre?>?>() {}.type

val GENRES: List<Genre> = Gson().fromJson(a, collectionType)


fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    return format.format(date)
}