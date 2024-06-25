package com.falikiali.rschapp.presentation.main.updateprofile

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
import com.falikiali.rschapp.databinding.FragmentUpdateProfileBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    private val binding: FragmentUpdateProfileBinding by lazy { FragmentUpdateProfileBinding.inflate(layoutInflater) }
    private val viewModel: UpdateProfileViewModel by viewModels()

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
        onClickBtnDone()
        onClickBack()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                tfFullName.isEnabled = it !is ResultState.Loading
                tfUserName.isEnabled = it !is ResultState.Loading
                btnDone.isEnabled = it !is ResultState.Loading
                progressBar.isVisible = it is ResultState.Loading
            }

            if (it is ResultState.Success) {
                showSnackBar("Update successfully")
                findNavController().navigate(R.id.action_global_dashboardFragment)
            } else if (it is ResultState.Failed) {
                showSnackBar(it.error)
            }
        }

        viewModel.isUsernameValid.observe(viewLifecycleOwner) {
            if (it || binding.edUserName.text.toString().isBlank()) {
                binding.tfUserName.isErrorEnabled = false
                binding.tfUserName.error = null
            } else {
                binding.tfUserName.error = getString(R.string.username_helper)
            }
        }

        viewModel.isFullnameValid.observe(viewLifecycleOwner) {
            if (it || binding.edFullName.text.toString().isBlank()) {
                binding.tfFullName.isErrorEnabled = false
                binding.tfFullName.error = null
            } else {
                binding.tfFullName.error = getString(R.string.fullname_helper)
            }
        }

        viewModel.isBtnDoneEnabled.observe(viewLifecycleOwner) {
            binding.btnDone.isEnabled = it
        }
    }

    private fun setListenerTextField() {
        binding.edFullName.doOnTextChanged { text, _, _, _ ->
            viewModel.checkFullnameValid(text.toString())
        }

        binding.edUserName.doOnTextChanged { text, _, _, _ ->
            viewModel.checkUsernameValid(text.toString())
        }
    }

    private fun onClickBtnDone() {
        binding.btnDone.setOnClickListener {
            viewModel.updateProfile(
                binding.edFullName.text.toString(),
                binding.edUserName.text.toString()
            )
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

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}