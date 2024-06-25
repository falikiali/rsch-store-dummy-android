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

    /**
     * Convert currency
     */
    private val locale = Locale("id", "ID")
    private val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0
    }

    fun convertIntToRupiah(price: Int): String = formatter.format(price)

}