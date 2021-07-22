package com.example.lawatchlist.ui.fragments.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.FragmentFavoriteMoviesBinding
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.model.toDomain
import com.example.lawatchlist.ui.fragments.adapter.MovieAdapter
import com.example.lawatchlist.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoritesFragment : Fragment() {
//TODO Implement local search for favorited items
    private lateinit var adapter: MovieAdapter
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        val view = binding.root
        setupUI()
        return view
    }

    private fun setupUI() {
        activity?.title = getString(R.string.favorites_fragment_title)

        adapter = MovieAdapter(isFavoritesFrmt = true, onClickListener = { movie, _ -> navigateToDetailScreen(movie) })
        _binding!!.movieListRecyclerView.layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)

        _binding!!.movieListRecyclerView.adapter = adapter
        ItemTouchHelper(itemTouChCallBack).attachToRecyclerView(binding.movieListRecyclerView)

        favoritesViewModel.favoriteMovies.observe(viewLifecycleOwner, { movies ->

            adapter.showMovies(movies.map { it.toDomain() })
        })


    }

    private fun navigateToDetailScreen(movie: Movie) {
        val action =
            FavoritesFragmentDirections.actionFavoriteScreenToDetailScreen(movie)
        findNavController().navigate(action)
    }


    private val itemTouChCallBack =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val deletedMovie = adapter.deleteMovie(position)
                favoritesViewModel.deleteMovie(deletedMovie)
                adapter.notifyItemRemoved(position)
                var deletedMovieName = getString(R.string.deleted_movie, deletedMovie.title)

                Snackbar.make(
                    binding.movieListRecyclerView,
                    deletedMovieName,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.undo), View.OnClickListener {

                        favoritesViewModel.addMovieBack()
                        adapter.addMovieBack()

                        adapter.notifyItemInserted(position)
                        deletedMovieName =
                            getString(R.string.re_added_movie, deletedMovie.title)
                        Toast.makeText(
                            activity?.applicationContext,
                            deletedMovieName,
                            Toast.LENGTH_SHORT
                        ).show()
                    }).show()


            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)

        // TODO implement room local search
        val searchView = menuItem.actionView as SearchView

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> { logOut() ; true }
            R.id.action_clear_list -> { favoritesViewModel.clearMovies() ; true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        activity?.getSharedPreferences("state", Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean("loggedStatus", false)
            ?.apply()

        val intent = Intent(activity, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}