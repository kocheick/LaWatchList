package com.example.lawatchlist.repository

import androidx.lifecycle.LiveData
import com.example.lawatchlist.convertLongToTime
import com.example.lawatchlist.model.MovieDBModel
import com.example.lawatchlist.model.networkmodelv2.toDomainModel
import com.example.lawatchlist.repository.database.MovieDao
import com.example.lawatchlist.repository.network.MovieApiService
import com.example.lawatchlist.repository.network.builApiService
import com.example.lawatchlist.toDBModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*
import java.time.temporal.TemporalUnit

class MovieRepository(private val movieDao: MovieDao) {

    var timestamp = Instant.now().toEpochMilli()
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val service: MovieApiService by lazy { builApiService() }

    fun getSearchResult() = movieDao.getSearchedMovies()

    fun getFavoriteMovies() = movieDao.getFavoritedMovies()

    suspend fun insert(movie: MovieDBModel) = movieDao.insertMovie(movie)

    suspend fun deleteMovie(movie: MovieDBModel) = movieDao.removeMovie(movie)

    suspend fun clearFavorites() = movieDao.deleteFavorites()

    suspend fun clearSearch() {
        val fav = movieDao.getFavoritedMovieList()
            .map { it.copy(isSearchItem = false, isFavorite = true) }
        updateList(fav)
        movieDao.clearSearchResult()
    }

    suspend fun updateMovie(movie: MovieDBModel) = movieDao.updateMovie(movie)


    suspend fun searchMovie(movieToSearch: String) {
        if (movieToSearch.isNotBlank()) {
            val fav = movieDao.getFavoritedMovieList()
                .map { it.copy(isSearchItem = false, isFavorite = true) }
            movieDao.clearSearchResult()

            val searchResultList = service.searchMovie(movieToSearch = movieToSearch)
            val moviesResult = searchResultList.toDomainModel().filter {
                it.posterPath != null ||
                        it.backdropPath != null
            }.filter { !it.overview.isNullOrEmpty() }

            if (fav.isNotEmpty()) updateList(fav)
            movieDao.saveSearchResult(moviesResult.toDBModel().map { it.copy(isSearchItem = true) })
        }
    }

    suspend fun getMovieById(id: Int): MovieDBModel? =
        movieDao.getMovieById(id)

    // 7200000 every 2h
    private fun isFetchNeeded(savedAt: Long): Boolean {

        // return savedAt + 10000 < System.currentTimeMillis()
        return true

    }


    fun getTrendingMovies(): LiveData<List<MovieDBModel>> {

        if (isFetchNeeded(timestamp)) {
            //timestamp = System.currentTimeMillis()
            coroutineScope.launch {
                val newData: List<MovieDBModel> =
                    service.getTrendingMovies().toDomainModel().toDBModel()
                        .map { it.copy(isTrending = true) }
                movieDao.clearTrendinMovies()
                movieDao.saveSearchResult(newData)
            }
        }

        return movieDao.getTrendingMovies()

    }

    fun getTopRated(): LiveData<List<MovieDBModel>> {
        val vids: LiveData<List<MovieDBModel>> = if (isFetchNeeded(timestamp)) {
            coroutineScope.launch {
                val newData = service.getTopRated().toDomainModel().toDBModel()
                    .map { it.copy(isTopRated = true) }
                movieDao.clearTopRatedMovies()
                movieDao.saveSearchResult(newData)

            }

            movieDao.getTopRatedMovies()
        } else {
            movieDao.getTopRatedMovies()
        }
        return vids

    }

    suspend fun updateList(fav: List<MovieDBModel>) = movieDao.updateList(fav)

    suspend fun searchFavorite(query: String): List<MovieDBModel> = movieDao.searchFavorites(query)
}