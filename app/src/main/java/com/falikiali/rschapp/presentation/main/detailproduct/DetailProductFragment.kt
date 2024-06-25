package com.falikiali.rschapp.presentation.main.detailproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.falikiali.rschapp.databinding.FragmentDetailProductBinding
import com.falikiali.rschapp.domain.model.DetailSizeProduct
import com.falikiali.rschapp.helper.ConstantData
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private val binding: FragmentDetailProductBinding by lazy { FragmentDetailProductBinding.inflate(layoutInflater) }
    private val navArgs: DetailProductFragmentArgs by navArgs()
    private val viewModel: DetailProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProductArgument()
        observeViewModel()
        onClickBtnBack()
        onBackPressed()
    }

    private fun getProductArgument() {
        val product = navArgs.product

        with(binding) {
            tvName.text = product.name
            tvPurpose.text = product.purpose
            tvPrice.text = ConstantData.convertIntToRupiah(product.price)
            tvDesc.text = product.description

            Glide.with(requireContext())
                .load(product.image)
                .into(ivThumbnail)
        }

        viewModel.getProduct(product.id, product.category)
        onClickBtnRetry(product.id, product.category)
        onClickBtnAddCart(product.id)
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                content.isVisible = it is ResultState.Success
                btnRetry.isVisible = it is ResultState.Failed
            }

            if (it is ResultState.Success) {
                binding.tvCategory.text = it.data.category
                chipSize(it.data.sizes)
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.resultAddCart.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                content.isVisible = it !is ResultState.Loading
                btnBuy.isEnabled = it !is ResultState.Loading
                btnAddToCart.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                showSnackBar("Successfully added product to cart")
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sizeProductSelected.collectLatest {
                    binding.btnAddToCart.isEnabled = it != ""
                    binding.btnBuy.isEnabled = it != ""
                }
            }
        }
    }

    private fun chipSize(sizes: List<DetailSizeProduct>) {
        sizes.forEachIndexed { index, size ->
            val newChip = Chip(context).apply {
                id = index
                text = if (size.size != "N/A") {
                    size.size + " (${size.stock})"
                } else {
                     "All size (${size.stock})"
                }
                isCheckable = true

                setOnClickListener {
                    isChecked = true
                    viewModel.updateSizeProductSelected(size.id)
                }
            }

            binding.chipSize.addView(newChip)
        }
    }

    private fun onClickBtnBack() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onClickBtnRetry(idProduct: String, idCategory: Int) {
        binding.btnRetry.setOnClickListener {
            viewModel.getProduct(idProduct, idCategory)
        }
    }

    private fun onClickBtnAddCart(idProduct: String) {
        binding.btnAddToCart.setOnClickListener {
            viewModel.addCart(idProduct)
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