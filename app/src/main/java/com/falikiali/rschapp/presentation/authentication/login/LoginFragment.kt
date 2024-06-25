package com.falikiali.rschapp.presentation.authentication.login

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
import com.falikiali.rschapp.databinding.FragmentLoginBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListenerTextField()
        observeViewModel()
        onClickBtnRegister()
        onClickBack()
        onClickBtnLogin()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                btnLogin.isEnabled = it !is ResultState.Loading
                btnRegister.isEnabled = it !is ResultState.Loading
                tfEmail.isEnabled = it !is ResultState.Loading
                tfPassword.isEnabled = it !is ResultState.Loading
                progressBar.isVisible = it is ResultState.Loading
            }

            if (it is ResultState.Failed) {
                showSnackBar(it.error)
            } else if (it is ResultState.Success) {
                showSnackBar("Login successful")
                viewModel.setAccessToken(it.data.accessToken)
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

        viewModel.isBtnLoginEnabled.observe(viewLifecycleOwner) {
            binding.btnLogin.isEnabled = it
        }
    }

    private fun setListenerTextField() {
        binding.edEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.checkEmailValid(text.toString())
        }

        binding.edPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkPasswordValid(text.toString())
        }
    }

    private fun onClickBtnRegister() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun onClickBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().previousBackStackEntry != null) {
                    findNavController().popBackStack()
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    private fun onClickBtnLogin() {
        binding.btnLogin.setOnClickListener {

            viewModel.login(
                binding.edEmail.text.toString(),
                binding.edPassword.text.toString()
            )
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}