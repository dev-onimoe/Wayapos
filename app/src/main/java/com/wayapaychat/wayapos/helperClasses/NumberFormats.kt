package com.wayapaychat.wayapos.helperClasses

import android.icu.text.NumberFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class NumberFormats {

    companion object {

        @RequiresApi(Build.VERSION_CODES.N)
        fun formatToNaira(amount: Number) = NumberFormat.getInstance(Locale.US)
            .format(amount).let {
                "₦${it.replace("-", "")}"
            }

        fun formatToNGN(data : Double) : String{
            val newformat: java.text.NumberFormat = java.text.NumberFormat.getCurrencyInstance()
            newformat.setMaximumFractionDigits(0)
            newformat.setCurrency(Currency.getInstance("NGN"))
            return newformat.format(data)
        }
    }

}