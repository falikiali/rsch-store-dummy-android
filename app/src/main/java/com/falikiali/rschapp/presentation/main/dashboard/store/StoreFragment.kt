package com.falikiali.rschapp.presentation.main.dashboard.store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentStoreBinding
import com.falikiali.rschapp.helper.ResultState
import com.falikiali.rschapp.presentation.main.dashboard.DashboardFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private val binding: FragmentStoreBinding by lazy { FragmentStoreBinding.inflate(layoutInflater) }
    private val storeCategoryAdapter: StoreCategoryAdapter by lazy { StoreCategoryAdapter() }
    private val productPagingAdapter: ProductPagingAdapter by lazy { ProductPagingAdapter() }
    private val viewModel: StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategories()
        initRv()
        observeViewModel()
        handleStatePaging()
        setListenerTextField()
        onClickItemCategory()
        onClickItemProduct()
        onClickBtnRetry()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateSelectedCategory(0)
    }

    private fun initRv() {
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = storeCategoryAdapter
        }

        binding.rvProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productPagingAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                binding.rvProduct.scrollToPosition(0)
                productPagingAdapter.submitData(it)
            }
        }

        viewModel.resultCategories.observe(viewLifecycleOwner) {
            with(binding) {
                rvCategory.isVisible = it is ResultState.Success
            }

            if (it is ResultState.Success) {
                storeCategoryAdapter.submitList(it.data)
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCategory.collectLatest {
//                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchProduct.collectLatest {
//                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleStatePaging() {
        viewLifecycleOwner.lifecycleScope.launch {
            productPagingAdapter.loadStateFlow.collectLatest { loadStates ->
                with(binding) {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    btnRetry.isVisible = loadStates.refresh is LoadState.Error
                    rvProduct.isVisible = loadStates.refresh is LoadState.NotLoading && productPagingAdapter.itemCount != 0
                }
            }
        }
    }

    private fun setListenerTextField() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchProduct(newText!!)
                return true
            }
        })
    }

    private fun onClickItemCategory() {
        storeCategoryAdapter.onClickItem = {
            viewModel.updateSelectedCategory(it.id)
        }
    }

    private fun onClickItemProduct() {
        productPagingAdapter.onClickItem = { product ->
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(DashboardFragmentDirections.actionDashboardFragmentToDetailProductFragment(
                product
            ))
        }
    }

    private fun onClickBtnRetry() {
        binding.btnRetry.setOnClickListener {
            viewModel.getCategories()
            productPagingAdapter.refresh()
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}