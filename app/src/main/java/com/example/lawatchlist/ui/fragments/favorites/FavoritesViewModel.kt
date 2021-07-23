package com.example.lawatchlist.ui.fragments.favorites

import android.app.Application
import androidx.lifecycle.*
import com.example.lawatchlist.model.*
import com.example.lawatchlist.repository.MovieRepository
import com.example.lawatchlist.repository.database.MovieDatabase
import com.example.lawatchlist.toDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private lateinit var _liveDataMovies: MutableLiveData<List<MovieDBModel>>
    val favoriteMovies: LiveData<List<MovieDBModel>> get() = _liveDataMovies

    private val _movieToDelete: MutableLiveData<Movie> by lazy { MutableLiveData<Movie>() }
    private val movieToDelete: LiveData<Movie> get() = _movieToDelete


    init {
        val movieDao = MovieDatabase.getDatabase(
            application,
            viewModelScope
        ).movieDao()
        repository = MovieRepository(movieDao)

        refreshFavoriteMovies()

    }

    private fun refreshFavoriteMovies() = viewModelScope.launch {
        // map() return a new list otherwise you'll be tracking room livedata which will throw an err
        _liveDataMovies =
            repository.getFavoriteMovies().map { it } as MutableLiveData<List<MovieDBModel>>
    }


    fun addMovieBack() = viewModelScope.launch(Dispatchers.IO) {
        // repository.insert(movieToDelete.value!!.toDBModel())
        val searchItemUpdate: MovieDBModel? = repository.getMovieById(movieToDelete.value!!.id!!)
        if (searchItemUpdate != null) {
            repository.updateMovie(searchItemUpdate.copy(isFavorite = true))
        }
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        _movieToDelete.postValue(movie)
        val searchItemUpdate: MovieDBModel = repository.getMovieById(movie.id!!)!!

        if (!searchItemUpdate.isTopRated && !searchItemUpdate.isTrending && !searchItemUpdate.isSearchItem) repository.deleteMovie(
            movie.toDBModel()
        )
        else repository.updateMovie(searchItemUpdate.copy(isFavorite = false))


    }

    fun clearMovies() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearFavorites()
    }

    fun searchFavorite(query: String) {
        viewModelScope.launch {
            when {
                query.isNotBlank() -> {
                    val sanitizedQuery = sanitizeSearchQuery(query)

                    repository.searchFavorite(sanitizedQuery).let {
                        _liveDataMovies.value = it
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun sanitizeSearchQuery(query: String): String {
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }
}