package com.falikiali.rschapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val email: String,
    val fullname: String,
    val username: String,
    val phoneNumber: String
) : Parcelable
