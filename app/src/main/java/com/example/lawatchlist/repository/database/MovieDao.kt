package com.example.lawatchlist.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.MovieDBModel


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDBModel)

    @Delete
    suspend fun removeMovie(movie: MovieDBModel)

    @Update
   suspend  fun updateMovie(movie: MovieDBModel)

    @Query("SELECT * FROM movie_table WHERE id IN(:movieId)")
    suspend fun getMovieById(movieId: Int): MovieDBModel?

    // Favorites

    @Query("""
  SELECT *FROM movie_table
  JOIN movie_fts ON movie_table.title = movie_fts.title AND movie_table.isFavorite =1
  WHERE movie_fts MATCH :query
""")
    suspend fun searchFavorites(query:String) : List<MovieDBModel>

    @Query("SELECT * FROM movie_table WHERE isFavorite ==1 /*:isFavorite*/ ")
    fun getFavoritedMovies(/*isFavorite: Boolean = true*/): LiveData<List<MovieDBModel>>

 @Query("SELECT * FROM movie_table WHERE isFavorite ==1 /*:isFavorite*/ ")
 suspend fun getFavoritedMovieList(/*isFavorite: Boolean = true*/): List<MovieDBModel>

    @Query("DELETE FROM movie_table WHERE isFavorite = 1")
    suspend fun deleteFavorites()


    //Search
    @Update
    suspend fun updateList(movies: List<MovieDBModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun saveSearchResult(searchResult: List<MovieDBModel>)

    @Query("SELECT * FROM movie_table WHERE isSearchItem = 1")
     fun getSearchedMovies(): LiveData<List<MovieDBModel>>


    @Query("DELETE FROM movie_table WHERE (isSearchItem = 1 AND isTopRated = 0 AND isTrending = 0 AND isFavorite = 0)")
    suspend fun clearSearchResult()

    //Home
    @Query("SELECT * FROM movie_table WHERE isTrending = 1")
     fun getTrendingMovies(): LiveData<List<MovieDBModel>>

    @Query("SELECT * FROM movie_table WHERE isTopRated = 1")
     fun getTopRatedMovies() : LiveData<List<MovieDBModel>>

    @Query("DELETE FROM movie_table WHERE isTrending = 1 ")
     suspend fun clearTrendinMovies()

    @Query("DELETE FROM movie_table WHERE isTopRated =1 ")
    suspend fun clearTopRatedMovies()




}