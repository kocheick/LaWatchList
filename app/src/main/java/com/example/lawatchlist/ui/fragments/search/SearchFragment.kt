package com.example.lawatchlist.ui.fragments.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.FragmentSearchMovieBinding
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.toDomain
import com.example.lawatchlist.ui.fragments.adapter.MovieAdapter
import com.example.lawatchlist.ui.login.LoginActivity
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SearchFragment : Fragment() {
    private lateinit var adapter: MovieAdapter
    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        val view = binding.root


        setupUI()

        return view
    }


    private fun setupUI() {
        activity?.title = getString(R.string.search_fragment_title)
        adapter = MovieAdapter ({ movie, view ->
            when (view.id) {
                R.id.movieCard -> navigateToDetailScreen(movie)
                R.id.favorite_button_view -> {
                    if (movie.isFavorite) {
                        (view as LottieAnimationView).apply {
                            progress = 0.97524375f
                            speed = -1.5f
                            playAnimation() } }
                    else if (!movie.isFavorite) (view as LottieAnimationView).apply {
                        progress = 0.0f
                        speed= 1.5f
                        playAnimation()
                    }
                        toggleFavorite(movie)
                }
            }
        } )
        _binding?.movieListRecyclerView?.layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)

        _binding?.movieListRecyclerView?.adapter = adapter
        viewModel.movieListUI.observe(viewLifecycleOwner, Observer { movies ->
                adapter.showMovies(movies.map { it.toDomain() })
        })

        viewModel.showLoading.observe(viewLifecycleOwner){ showLoading ->
            if (showLoading) {
                binding.progressBar.visibility =View.VISIBLE
                binding.movieListRecyclerView.visibility = View.GONE
            } else {_binding!!.progressBar.visibility = View.GONE
                binding.movieListRecyclerView.visibility = View.VISIBLE
            }
        }

    }



    private fun navigateToDetailScreen(movie: Movie) {
        val action =
            SearchFragmentDirections.actionSearchScreenToDetailScreen(
                movie
            )
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logOut()
                true
            }
            R.id.action_clear_list -> {
                viewModel.clearSearchedMovies()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search(query: String?) {
        if (query != null) {
            viewModel.searchMovie(query)
        }
    }

    private fun logOut() {
        activity?.getSharedPreferences("state", Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean("loggedStatus", false)
            ?.apply()

        val intent = Intent(activity, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
    }

    private fun toggleFavorite(movie: Movie) {
        val isFavorite = movie.isFavorite
        val newState = movie.copy(isFavorite = isFavorite.not())

        if (isFavorite) viewModel.unfavoriteMovie(movie)
        else viewModel.favoriteMovie(newState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}