package com.example.lawatchlist.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.ActivityMainBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*


@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setupNavComponent()
    }



    private fun setupNavComponent() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        //hide bottom nav bar when detail screen is opened
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        // set global navigation
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.searchFragment -> {
                   navController.navigate(R.id.action_global_to_search_fragment); true
                }
                R.id.favoritesFragment -> {
                    navController.navigate(R.id.action_global_to_favorite_fragment); true
                }
                R.id.homeFragment -> {
                   navController.navigate(R.id.action_global_home_Fragment); true
                }

                else -> {false }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}