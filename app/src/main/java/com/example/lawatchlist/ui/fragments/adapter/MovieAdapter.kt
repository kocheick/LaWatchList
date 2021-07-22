package com.example.lawatchlist.ui.fragments.adapter

import android.os.Build
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lawatchlist.databinding.MovieListItemBinding
import com.example.lawatchlist.model.Movie
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MovieAdapter(
    private val onClickListener: ((Movie, View) -> Unit),
    private val isFavoritesFrmt: Boolean = false
) : RecyclerView.Adapter<ViewHolder>() {
    var movies = mutableListOf<Movie>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MovieListItemBinding =
            MovieListItemBinding.inflate(from(parent.context), parent, false)


        return ViewHolder(
            binding = binding,
            isFavoritesFrmt = isFavoritesFrmt,
            onClickListener = { position, view ->
                val currentMovie = movies[position]
                onClickListener(currentMovie, view)
            }
        )

    }


    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun showMovies(movies: List<Movie>) {
        this.movies.clear()
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
}

// delete and add-back movie methods from adapter

private lateinit var deletedMovie: Movie
private var deletedMoviePosition: Int = 0

fun deleteMovie(pos: Int): Movie {
    deletedMovie = movies[pos]
    deletedMoviePosition = pos
    notifyItemRemoved(pos)
    movies.removeAt(pos)
    return deletedMovie
}

fun addMovieBack() {
    movies.add(deletedMoviePosition, deletedMovie)

}

}