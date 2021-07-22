package com.example.lawatchlist.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lawatchlist.R
import com.example.lawatchlist.model.MovieDBModel
import com.example.lawatchlist.utils.DataConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@Database(
    entities = [MovieDBModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    @InternalCoroutinesApi
    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MovieDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        context.getString(R.string.database_name)
                    ).addCallback(MovieDatabaseCallback(scope))

                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun movieDao(): MovieDao


    private class MovieDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        @InternalCoroutinesApi
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    println("Database created..!")
                    database.movieDao()

                        // populateDatabase(db)

                }
            }
        }

//        private fun populateDatabase(db: MovieDao) {
//            scope.launch(Dispatchers.IO) {
//                val movies = MovieStore.getAllMovies()
//                for (movie in movies) {
//                    db.insertMovie(movie)
//                    println("${movie.title} added to DB")
//                }
//            }
//        }
    }
}


