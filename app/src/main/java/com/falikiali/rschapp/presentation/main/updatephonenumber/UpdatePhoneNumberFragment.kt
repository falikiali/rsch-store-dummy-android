package com.falikiali.rschapp.presentation.main.updatephonenumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentUpdatePhoneNumberBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePhoneNumberFragment : Fragment() {

    private val binding: FragmentUpdatePhoneNumberBinding by lazy { FragmentUpdatePhoneNumberBinding.inflate(layoutInflater) }
    private val viewModel: UpdatePhoneNumberViewModel by viewModels()

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
        setListenerTextField()
        onClickBack()
        onClickToolbarNavigationIcon()
        onClickBtnDone()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                btnDone.isEnabled = it !is ResultState.Loading
                tfNewPhoneNumber.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                showSnackBar("Update phone number successfully")
                findNavController().navigate(UpdatePhoneNumberFragmentDirections.actionUpdatePhoneNumberFragmentToDashboardFragment())
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.isPhoneNumberValid.observe(viewLifecycleOwner) {
            if (it || binding.edNewPhoneNumber.text.toString().isBlank()) {
                binding.tfNewPhoneNumber.isErrorEnabled = false
                binding.tfNewPhoneNumber.error = null
            } else {
                binding.tfNewPhoneNumber.error = getString(R.string.new_phone_number_helper)
            }

            binding.btnDone.isEnabled = it
        }
    }

    private fun setListenerTextField() {
        binding.edNewPhoneNumber.doOnTextChanged { text, _, _, _ ->
            viewModel.checkPhoneNumberValid(text.toString())
        }
    }

    private fun onClickBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun onClickToolbarNavigationIcon() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onClickBtnDone() {
        binding.btnDone.setOnClickListener {
            viewModel.updatePhoneNumber(binding.edNewPhoneNumber.text.toString())
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}