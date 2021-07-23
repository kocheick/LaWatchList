package com.example.lawatchlist.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.FragmentHomeBinding
import com.example.lawatchlist.model.Movie
import com.example.lawatchlist.ui.fragments.home.adapter.HomeAdapter
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment() {
    //TODO implement caching for trending and top rated items

    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        activity?.actionBar?.hide()
        setupUI()
        return view
    }

    private fun setupUI() {
        activity?.title = getString(R.string.home_fragment_title)
        val trendingAdapter = HomeAdapter()
        val topRatedAdapter = HomeAdapter()

        with(_binding!!) {
            trendingRecyclerView.layoutManager = LinearLayoutManager(
                activity?.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            trendingRecyclerView.adapter = trendingAdapter
            trendingAdapter.clickListener = { navigateToDetailScreen(it)}

            topRatedRecyclerView.layoutManager = LinearLayoutManager(
                activity?.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            topRatedRecyclerView.adapter = topRatedAdapter
topRatedAdapter.clickListener = {navigateToDetailScreen(it)}
        }
        viewModel.trendingMovies.observe(viewLifecycleOwner, { list ->
            list?.let {
                trendingAdapter.showMovies(it)
            }
        })
        viewModel.topRated.observe(viewLifecycleOwner, { list ->
            list?.let {
                topRatedAdapter.showMovies(it)
            }
        })
        viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.linearLayout.visibility = View.GONE
            } else {
                _binding!!.progressBar.visibility = View.GONE
                binding.linearLayout.visibility = View.VISIBLE
            }
        }


    }

    private fun navigateToDetailScreen(movie: Movie) {
        val action =
            HomeFragmentDirections.actionHomeScreenToDetailScreen(movie)
        findNavController().navigate(action)
    }


}