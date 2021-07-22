package com.example.lawatchlist.ui.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.FragmentMovieDetailBinding
import com.example.lawatchlist.*
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.utils.ImageLoader


class DetailFragment : Fragment() {
    // TODO update detail view with backCover
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val movie: Movie by lazy { DetailFragmentArgs.fromBundle(requireArguments()).movieToDisplay }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        setupView(movie)

        return view
    }


    private fun setupView(movie: Movie) {
        with(_binding!!) {
            val coverUrl = movie.images?.backdropPath ?: ""
        //    val duration = if (movie.duration != null) getString(R.string.movie_duration, movie.duration) else "Time N/A"

            titleView.text = movie.title
            summaryView.text = movie.summary
          //  durationView.text = duration
            releaseDateView.text = movie.releaseDate
            ImageLoader.load(coverView, coverUrl)

            if (movie.isFavorite) {
                favoriteButtonView.setBackgroundResource(R.drawable.ic_favoris_red)
            } else {
                favoriteButtonView.setBackgroundResource(R.drawable.ic_favorite)
            }

        }
    }
}