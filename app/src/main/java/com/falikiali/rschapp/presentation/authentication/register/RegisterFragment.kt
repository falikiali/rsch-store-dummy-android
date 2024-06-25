package com.falikiali.rschapp.presentation.authentication.register

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
import com.falikiali.rschapp.databinding.FragmentRegisterBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val binding: FragmentRegisterBinding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModels()

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
        onClickBtnLogin()
        onClickBack()
        onClickBtnRegister()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                tfEmail.isEnabled = it !is ResultState.Loading
                tfPassword.isEnabled = it !is ResultState.Loading
                tfConfirmPassword.isEnabled = it !is ResultState.Loading
                btnRegister.isEnabled = it !is ResultState.Loading
                btnLogin.isEnabled = it !is ResultState.Loading
                progressBar.isVisible = it is ResultState.Loading
            }

            if (it is ResultState.Success) {
                showSnackBar("Register successful")
                viewModel.setAccessToken(it.data.accessToken)
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.isEmailValid.observe(viewLifecycleOwner) {
            if (it || binding.edEmail.text.toString().isBlank()) {
                binding.tfEmail.isErrorEnabled = false
                binding.tfEmail.error = null
            } else {
                binding.tfEmail.error = getString(R.string.email_helper)
            }
        }

        viewModel.isPasswordValid.observe(viewLifecycleOwner) {
            if (it || binding.edPassword.text.toString().isBlank()) {
                binding.tfPassword.isErrorEnabled = false
                binding.tfPassword.error = null
            } else {
                binding.tfPassword.error = getString(R.string.password_helper)
            }
        }

        viewModel.isConfirmPasswordValid.observe(viewLifecycleOwner) {
            if (it || binding.edConfirmPassword.text.toString().isBlank()) {
                binding.tfConfirmPassword.isErrorEnabled = false
                binding.tfConfirmPassword.error = null
            } else {
                binding.tfConfirmPassword.error = getString(R.string.confirm_password_helper)
            }
        }

        viewModel.isBtnRegisterEnabled.observe(viewLifecycleOwner) {
            binding.btnRegister.isEnabled = it
        }
    }

    private fun setListenerTextField() {
        binding.edEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.checkEmailValid(text.toString())
        }

        binding.edPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkPasswordValid(text.toString())
        }

        binding.edConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkConfirmPasswordValid(text.toString(), binding.edPassword.text.toString())
        }
    }

    private fun onClickBtnLogin() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onClickBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun onClickBtnRegister() {
        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.edEmail.text.toString(),
                binding.edPassword.text.toString(),
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