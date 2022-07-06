package com.wayapaychat.wayapay.presentation.utils.ext.views

import java.text.NumberFormat
import java.util.Currency

fun String.removeCommas(): String {
    return this.replace(",", "")
}

internal fun formatToNaira(data : Double) : String{
    val newformat: NumberFormat = NumberFormat.getCurrencyInstance()
    newformat.setMaximumFractionDigits(0)
    newformat.setCurrency(Currency.getInstance("NGN"))
    return newformat.format(data)
}