package com.example.lawatchlist.ui.fragments.home.adapter

import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.example.lawatchlist.databinding.HomeMovieItemBinding
import com.example.lawatchlist.GENRES
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.networkmodelv2.MovieTMDB
import com.example.lawatchlist.utils.ImageLoader

class HomeViewHolder(private val binding: HomeMovieItemBinding, val clickListener: (Movie) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {

        with(binding) {
            val hm = HashMap<Int, String>()
            for (i in GENRES) hm[i.id] = i.name
            val genres: List<String> = movie.genreIds.map { hm.get(it)!! }


            homeMovieItem.setOnClickListener { clickListener(movie) }
            homeMovieTitle.text = movie.title
            homeMovieGenre.text = genres.toString().substringBefore("]").substringAfter("[")
            val coverUrl: String = movie.images?.posterPath ?: ""

            ImageLoader.load(homeMovieCover, coverUrl)


        }
    }
}