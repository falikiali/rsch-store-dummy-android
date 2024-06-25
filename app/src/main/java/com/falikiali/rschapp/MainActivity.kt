package com.falikiali.rschapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.falikiali.rschapp.databinding.ActivityMainBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoggedIn.observe(this@MainActivity) { loggedIn ->
            if (loggedIn) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
                navGraph.setStartDestination(R.id.dashboardFragment)
                navController.graph = navGraph
            } else {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
                navGraph.setStartDestination(R.id.loginFragment)
                navController.graph = navGraph
            }
        }
    }

}