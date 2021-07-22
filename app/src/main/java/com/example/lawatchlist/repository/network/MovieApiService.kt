package com.example.lawatchlist.repository.network

import com.example.lawatchlist.model.networkmodelv2.APIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("search/movie/")
    suspend fun searchMovie(@Query("api_key") apiKey : String = API_KEY , @Query("query") movieToSearch: String, @Query("page") page:Int = 1) : APIResponse

    @GET("movie/popular")
    suspend fun getTrendingMovies(@Query("api_key") apiKey : String = API_KEY) : APIResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") apiKey : String = API_KEY) : APIResponse


}