package com.falikiali.rschapp.presentation.main.dashboard.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {

    private val binding: FragmentWishlistBinding by lazy { FragmentWishlistBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}