package com.falikiali.rschapp.helper

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

object GeneralHelper {

    private val locale = Locale("id", "ID")
    private val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0
    }

    /**
     * Helper for fragment
     */
    fun Fragment.showSnackBar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Helper for activity
     */
    fun Activity.showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Convert currency
     */
    fun convertIntToRupiah(price: Int): String {
        return formatter.format(price)
    }

}