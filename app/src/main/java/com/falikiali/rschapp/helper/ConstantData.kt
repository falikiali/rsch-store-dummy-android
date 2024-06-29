package com.falikiali.rschapp.helper

import androidx.datastore.preferences.core.stringPreferencesKey
import java.text.NumberFormat
import java.util.Locale

object ConstantData {

    /**
     * Data store
     */
    const val APP_PREFERENCES = "app_preferences"
    val ACCESS_TOKEN = stringPreferencesKey("access_token")

}