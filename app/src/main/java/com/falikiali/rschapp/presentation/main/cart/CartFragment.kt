package com.falikiali.rschapp.presentation.main.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.falikiali.rschapp.MainViewModel
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentCartBinding
import com.falikiali.rschapp.helper.ConstantData
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val binding: FragmentCartBinding by lazy { FragmentCartBinding.inflate(layoutInflater) }
    private val cartAdapter: CartAdapter by lazy { CartAdapter() }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

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
        onUpdateQuantityItem()
        onClickBtnRemoveItem()
        onClickBtnBuy()
        onClickToolbarNavigationIcon()
        onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.updateProductInCart()
        mainViewModel.clearChangedProduct()
    }

    private fun initRv() {
        binding.rvProductCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                rvProductCart.isVisible = it is ResultState.Success
                btnBuy.isEnabled = it is ResultState.Success
            }

            if (it is ResultState.Success) {
                cartAdapter.submitList(it.data)
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.resultDeleteProduct.observe(viewLifecycleOwner) {
            if (it is ResultState.Success) {
                viewModel.getProductInCart()
                showSnackBar("The product was successfully deleted")
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.resultUpdateSelectedProduct.observe(viewLifecycleOwner) {
            if (it is ResultState.Success) {
                showSnackBar("The product was successfully updated")
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
                mainViewModel.clearChangedProduct()
            }
        }

        mainViewModel.resultUpdateCart.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                rvProductCart.isVisible = it !is ResultState.Loading
                btnBuy.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                viewModel.getProductInCart()
                mainViewModel.clearChangedProduct()
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }
    }

    private fun onUpdateQuantityItem() {
        cartAdapter.onClickBtnIncrease = {
            val changedProduct = mutableListOf<ChangedProductCart>()

            it.forEach { (k, v) ->
                changedProduct.add(
                    ChangedProductCart(k ,v)
                )
            }

            mainViewModel.updateChangedProduct(changedProduct)
        }

        cartAdapter.onClickBtnDecrease = {
            val changedProduct = mutableListOf<ChangedProductCart>()

            it.forEach { (k, v) ->
                changedProduct.add(
                    ChangedProductCart(k ,v)
                )
            }

            mainViewModel.updateChangedProduct(changedProduct)
        }

        cartAdapter.onUpdateSelectedProduct = {
            var totalQty = 0
            var totalPay = 0

            it.forEach { (_, v) ->
                totalQty += v.first
                totalPay += (v.second * v.first)
            }

            binding.tvTotal.text = ConstantData.convertIntToRupiah(totalPay)
            binding.btnBuy.text = getString(R.string.buy) + " ($totalQty)"
        }

        cartAdapter.onClickBtnSelect = { id, isSelected ->
            viewModel.updateSelectedProductInCart(id, isSelected)
        }
    }

    private fun onClickBtnRemoveItem() {
        cartAdapter.onClickBtnRemove = {
            viewModel.deleteEachProductInCart(it.id)
        }
    }

    private fun onClickBtnBuy() {
        binding.btnBuy.setOnClickListener {
            mainViewModel.updateProductInCart()
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

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}