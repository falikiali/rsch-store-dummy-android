package com.falikiali.rschapp.presentation.main.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentDashboardBinding
import com.falikiali.rschapp.domain.model.Profile
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val binding: FragmentDashboardBinding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var profile: Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        onClickToolbarMenu()
        onClickBtnRetry()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                toolbar.isVisible = it is ResultState.Success
                toolbarDivider.isVisible = it is ResultState.Success
                dashboardNavHostFragment.isVisible = it is ResultState.Success
                btnRetry.isVisible = it is ResultState.Failed
                progressBar.isVisible = it is ResultState.Loading
                bottomNav.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                if (it.data.username == "" || it.data.fullname == "") {
                    findNavController().navigate(R.id.action_global_updateProfileFragment)
                }

                binding.toolbar.title = it.data.fullname
                binding.toolbar.subtitle = it.data.username

                val navHostFragment = childFragmentManager.findFragmentById(R.id.dashboard_nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                val navGraph = navController.navInflater.inflate(R.navigation.dashboard_bottom_nav_graph)
                navController.graph = navGraph
                binding.bottomNav.setupWithNavController(navController)

                profile = it.data
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }
    }

    private fun onClickToolbarMenu() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu -> {
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToProfileFragment(
                        profile
                    ))
                    true
                }
                R.id.cart -> {
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToCartFragment())
                    true
                }
                R.id.notification -> { false }
                else -> { false }
            }
        }
    }

    private fun onClickBtnRetry() {
        binding.btnRetry.setOnClickListener {
            viewModel.getProfile()
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}