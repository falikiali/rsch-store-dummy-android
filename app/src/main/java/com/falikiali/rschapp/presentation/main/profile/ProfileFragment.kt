package com.falikiali.rschapp.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.falikiali.rschapp.databinding.FragmentProfileBinding
import com.falikiali.rschapp.helper.ResultState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val binding: FragmentProfileBinding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val navArgs: ProfileFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfileArgument()
        observeViewModel()
        onClickBack()
        onClickToolbarNavigationIcon()
        onClickBtnUpdatePhoneNumber()
        onClickBtnUpdatePassword()
        onClickBtnLogout()
    }

    private fun getProfileArgument() {
        val profile = navArgs.profile

        with(binding) {
            tvFullname.text = profile.fullname
            tvUsername.text = profile.username
            tvEmail.text = profile.email
            tvPhoneNumber.text = if (profile.phoneNumber != "") "+${profile.phoneNumber}" else "-"
        }
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it is ResultState.Loading
                btnLogout.isEnabled = it !is ResultState.Loading
                btnChangePassword.isEnabled = it !is ResultState.Loading
                btnChangePhoneNumber.isEnabled = it !is ResultState.Loading
            }

            if (it is ResultState.Success) {
                viewModel.clearData()
            } else if (it is ResultState.Failed) {
                if (it.code == 401) {
                    viewModel.clearData()
                }
                showSnackBar(it.error)
            }
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

    private fun onClickBtnUpdatePhoneNumber() {
        binding.btnChangePhoneNumber.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdatePhoneNumberFragment())
        }
    }

    private fun onClickBtnUpdatePassword() {
        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())
        }
    }

    private fun onClickBtnLogout() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}