package com.example.lawatchlist.ui.fragments.home.adapter

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawatchlist.databinding.HomeMovieItemBinding
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.networkmodelv2.MovieTMDB

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {
    private var items = mutableListOf<Movie>()
    lateinit var clickListener : (Movie) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeMovieItemBinding.inflate(from(parent.context), parent, false)
        return HomeViewHolder(binding,clickListener)

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = items.size

    fun showMovies(newItems: List<Movie>) {
        this.items.clear()
        this.items = newItems.toMutableList()
        notifyDataSetChanged()
    }

}