package com.example.lawatchlist.ui.fragments.home

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.*
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.MovieDBModel
import com.example.lawatchlist.model.networkmodelv2.MovieTMDB
import com.example.lawatchlist.repository.MovieRepository
import com.example.lawatchlist.repository.database.MovieDatabase
import com.example.lawatchlist.repository.network.NetworkStatusChecker
import com.example.lawatchlist.toUIModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var _trending = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> by lazy { _trending }

    private var _topRated = MutableLiveData<List<Movie>>()
    val topRated: LiveData<List<Movie>> by lazy { _topRated }

    private var repository: MovieRepository

    private val _showLoading = MutableLiveData<Boolean>(true)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val networkStatusChecker: NetworkStatusChecker by lazy {
        NetworkStatusChecker(
            application.getSystemService(ConnectivityManager::class.java)
        )
    }

    init {
        val movieDao = MovieDatabase.getDatabase(
            application,
            viewModelScope
        ).movieDao()
        repository = MovieRepository(movieDao)

        refreshData()

    }

    private fun refreshData() {
        _showLoading.value = true
    networkStatusChecker.performIfConnectedToInternet {
        viewModelScope.launch {
            val trendings = repository.getTrendingMovies().map {it }.map { it.toUIModel() }
            _trending = trendings as MutableLiveData<List<Movie>>

            val topMovies = repository.getTopRated().map {it }.map { it.toUIModel() }
            _topRated = topMovies as MutableLiveData<List<Movie>>

            // _showLoading.value = trendingMovies.value.isNullOrEmpty() or topRated.value.isNullOrEmpty()
            delay(50)
            _showLoading.value = false

        }
    }


    }

}