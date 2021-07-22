package com.example.lawatchlist.ui.fragments.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.MovieListItemBinding
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.utils.ImageLoader

class ViewHolder(private val binding: MovieListItemBinding, private val onClickListener: (Int, View) -> Unit, private val isFavoritesFrmt :Boolean ) :
    RecyclerView.ViewHolder(binding.root) {

init {
    with(binding) {


    }
}
//TODO display item genres
    fun bind(movie: Movie  ) {

        with(binding) {
            if (isFavoritesFrmt) favoriteButtonView.visibility = View.INVISIBLE
            favoriteButtonView.setOnClickListener {
//                if (movie.isFavorite) {
//                    (it as LottieAnimationView).apply {
//                        progress = 0.97524375f
//                        speed = -1.5f
//                        playAnimation() } }
//                else (it as LottieAnimationView).apply {
//                    progress = 0.0f
//                    speed= 1.5f
//                    playAnimation()
//                }
                onClickListener(bindingAdapterPosition, it)
            }
            movieCard.setOnClickListener { onClickListener(bindingAdapterPosition, it) }


            val coverUrl = movie.images?.posterPath ?: ""
          //  val duration: String = if(movie.duration !=null ) itemView.context.getString(R.string.movie_duration, movie.duration) else ""
            titleView.text = movie.title
            summaryView.text = movie.summary
        //    durationView.text = duration
            releaseDateView.text = movie.releaseDate?.substringBefore("-")
            ImageLoader.load(coverView, coverUrl)

            if (movie.isFavorite)
                favoriteButtonView.progress = 0.97524375f
            else
                favoriteButtonView.progress = 0.0f



        }
    }


}