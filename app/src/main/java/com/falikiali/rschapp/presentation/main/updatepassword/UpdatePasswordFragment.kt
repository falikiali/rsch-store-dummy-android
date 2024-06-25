package com.falikiali.rschapp.presentation.main.updatepassword

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
import com.falikiali.rschapp.databinding.FragmentUpdatePasswordBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePasswordFragment : Fragment() {

    private val binding: FragmentUpdatePasswordBinding by lazy { FragmentUpdatePasswordBinding.inflate(layoutInflater) }
    private val viewModel: UpdatePasswordViewModel by viewModels()

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
                tfOldPassword.isEnabled = it !is ResultState.Loading
                tfNewConfirmPassword.isEnabled = it !is ResultState.Loading
                tfNewPassword.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                showSnackBar("Update password successfully")
                findNavController().navigate(UpdatePasswordFragmentDirections.actionUpdatePasswordFragmentToDashboardFragment())
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.isOldPasswordValid.observe(viewLifecycleOwner) {
            if (it || binding.edOldPassword.text.toString().isBlank()) {
                binding.tfOldPassword.isErrorEnabled = false
                binding.tfOldPassword.error = null
            } else {
                binding.tfOldPassword.error = getString(R.string.password_helper)
            }
        }

        viewModel.isNewPasswordValid.observe(viewLifecycleOwner) {
            if (it || binding.edNewPassword.text.toString().isBlank()) {
                binding.tfNewPassword.isErrorEnabled = false
                binding.tfNewPassword.error = null
            } else {
                binding.tfNewPassword.error = getString(R.string.new_password_helper)
            }
        }

        viewModel.isConfirmNewPasswordValid.observe(viewLifecycleOwner) {
            if (it || binding.edConfirmPassword.text.toString().isBlank()) {
                binding.tfNewConfirmPassword.isErrorEnabled = false
                binding.tfNewConfirmPassword.error = null
            } else {
                binding.tfNewConfirmPassword.error = getString(R.string.confirm_new_password_helper)
            }
        }

        viewModel.isBtnDoneEnabled.observe(viewLifecycleOwner) {
            binding.btnDone.isEnabled = it
        }
    }

    private fun setListenerTextField() {
        binding.edOldPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkOldPasswordValid(text.toString())
        }

        binding.edNewPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkNewPasswordValid(text.toString(), binding.edOldPassword.text.toString())
        }

        binding.edConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkConfirmNewPasswordValid(text.toString(), binding.edNewPassword.text.toString())
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
            viewModel.updatePassword(
                binding.edOldPassword.text.toString(),
                binding.edNewPassword.text.toString(),
                binding.edConfirmPassword.text.toString()
            )
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}