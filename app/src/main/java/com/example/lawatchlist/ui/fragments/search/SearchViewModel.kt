package com.example.lawatchlist.ui.fragments.search

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.*
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.MovieDBModel
import com.example.lawatchlist.repository.MovieRepository
import com.example.lawatchlist.repository.database.MovieDatabase
import com.example.lawatchlist.repository.network.NetworkStatusChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    private var _movieListUI = MutableLiveData<List<MovieDBModel>>()

    val movieListUI: LiveData<List<MovieDBModel>> get() = _movieListUI
    private val networkStatusChecker: NetworkStatusChecker by lazy {
        NetworkStatusChecker(
            application.getSystemService(ConnectivityManager::class.java)
        )
    }
    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    init {
        val movieDao = MovieDatabase.getDatabase(
            application,
            viewModelScope
        ).movieDao()

        repository = MovieRepository(movieDao)

        refreshResult()


    }


    fun favoriteMovie(movie: Movie) = viewModelScope.launch() {
        val searchItemUpdate = repository.getMovieById(movie.id!!)!!.apply {
            isFavorite = true
        }
        repository.updateMovie(searchItemUpdate)
//        repository.insert(movie.toDBModel())
        //  refreshResult()
//        TODO CHECK HERE IF SYNC PROBLEM
    }

    fun unfavoriteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        val searchItemUpdate = repository.getMovieById(movie.id!!)?.apply {
            isFavorite = false
        }!!

        repository.updateMovie(searchItemUpdate)
        //  repository.deleteMovie(movie.toDBModel())
        // refreshResult()
        //        TODO CHECK HERE IF SYNC PROBLEM

    }

    fun clearSearchedMovies() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearSearch()
        //refreshResult()
    }


    fun searchMovie(movieToSearch: String) = viewModelScope.launch(Dispatchers.IO) {
        _showLoading.postValue(true)

        networkStatusChecker.performIfConnectedToInternet {
            if (movieToSearch.isNotBlank()) {
                repository.searchMovie(movieToSearch)
            }

        }

        delay(500)
        _showLoading.postValue(false)

    }

    private fun refreshResult() = viewModelScope.launch {
        val data = repository.getSearchResult().map { it }
        _movieListUI = data as MutableLiveData<List<MovieDBModel>>

    }
}