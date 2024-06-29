package com.falikiali.rschapp.presentation.main.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.falikiali.rschapp.MainViewModel
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentCartBinding
import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.helper.ConstantData
import com.falikiali.rschapp.helper.GeneralHelper.showSnackBar
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.CartListener {

    private val binding: FragmentCartBinding by lazy { FragmentCartBinding.inflate(layoutInflater) }
    private val cartAdapter: CartAdapter by lazy { CartAdapter(this) }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    private var listIdChangedProductCart: List<String> = emptyList()
    private var listQuantityChangedProductCart: List<Int> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductInCart()
        initRv()
        observeViewModel()
        onClickBtnBuy()
        onClickToolbarNavigationIcon()
        onBackPressed()
    }

    override fun onDetach() {
        super.onDetach()

        if (listIdChangedProductCart.isNotEmpty() && listQuantityChangedProductCart.isNotEmpty()) {
            mainViewModel.updateProductInCart(listIdChangedProductCart, listQuantityChangedProductCart)
            mainViewModel.clearChangedProduct()
        }
    }

    override fun onBtnCheckItemClick(productCart: ProductCart, isChecked: Boolean) {
        viewModel.updateSelectedProductInCart(productCart.id, isChecked)
    }

    override fun onBtnIncreaseItemClick(changeProductCart: Map<String, Int>) {
        mainViewModel.updateChangedProduct(changeProductCart)
    }

    override fun onBtnDecreaseItemClick(changeProductCart: Map<String, Int>) {
        mainViewModel.updateChangedProduct(changeProductCart)
    }

    override fun onBtnRemoveItemClick(productCart: ProductCart) {
        viewModel.deleteEachProductInCart(productCart.id)
    }

    private fun initRv() {
        binding.rvProductCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            cartAdapter.setAdapterLoadingState(it is ResultState.Loading)
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                rvProductCart.isVisible = it is ResultState.Success
                btnBuy.isEnabled = it is ResultState.Success
            }

            if (it is ResultState.Success) {
                cartAdapter.submitList(it.data)

                val countSelectedProduct = it.data.filter { product -> product.isSelected }.size

                binding.contentProductSelected.isVisible = countSelectedProduct != 0
                binding.tvCountProductSelected.text = countSelectedProduct.toString() + " product selected"
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
                findNavController().popBackStack()
            }
        }

        viewModel.resultDeleteProduct.observe(viewLifecycleOwner) {
            cartAdapter.setAdapterLoadingState(it is ResultState.Loading)
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                btnBuy.isEnabled = it is ResultState.Success
            }

            if (it is ResultState.Success) {
                cartAdapter.submitList(it.data)

                val countSelectedProduct = it.data.filter { product -> product.isSelected }.size

                binding.contentProductSelected.isVisible = countSelectedProduct != 0
                binding.tvCountProductSelected.text = countSelectedProduct.toString() + " product selected"
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.resultUpdateSelectedProduct.observe(viewLifecycleOwner) {
            cartAdapter.setAdapterLoadingState(it is ResultState.Loading)
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                btnBuy.isEnabled = it is ResultState.Success
            }

            if (it is ResultState.Success) {
                cartAdapter.submitList(it.data)

                val countSelectedProduct = it.data.filter { product -> product.isSelected }.size

                binding.contentProductSelected.isVisible = countSelectedProduct != 0
                binding.tvCountProductSelected.text = countSelectedProduct.toString() + " product selected"
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        mainViewModel.changedProductCart.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                listIdChangedProductCart = emptyList()
                listQuantityChangedProductCart = emptyList()
            } else {
                val mutableListIdChangedProductCart = mutableListOf<String>()
                val mutableListQuantityChangedProductCart = mutableListOf<Int>()

                it.map { (k, v) ->
                    mutableListIdChangedProductCart.add(k)
                    mutableListQuantityChangedProductCart.add(v)
                }

                listIdChangedProductCart = mutableListIdChangedProductCart
                listQuantityChangedProductCart = mutableListQuantityChangedProductCart
            }
        }

        mainViewModel.resultUpdateCart.observe(viewLifecycleOwner) {
//            with(binding) {
//                progressBar.isVisible = it is ResultState.Loading
//                rvProductCart.isVisible = it !is ResultState.Loading
//                btnBuy.isEnabled = it !is ResultState.Loading
//            }
//
//            if (it is ResultState.Success) {
//                mainViewModel.clearChangedProduct()
//            } else if (it is ResultState.Failed) {
//                showSnackBar(it.error)
//            }
        }
    }

    private fun onClickBtnBuy() {
        binding.btnBuy.setOnClickListener {

        }
    }

    private fun onClickToolbarNavigationIcon() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

}