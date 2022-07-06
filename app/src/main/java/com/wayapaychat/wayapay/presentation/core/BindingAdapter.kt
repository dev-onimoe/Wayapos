package com.wayapaychat.wayapay.presentation.core

import android.graphics.Color
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

@BindingAdapter("convert_tag")
fun convertTag(tv: TextView, data: TransactionData) {
    if (data.status == "SUCCESSFUL") {
        tv.setBackgroundColor(Color.GREEN)
    } else if (data.status == "PENDING") {
        tv.setBackgroundColor(Color.YELLOW)
    } else if (data.status == "FAILED") {
        tv.setBackgroundColor(Color.RED)
    }else if (data.status == "ABANDONED") {
        tv.setBackgroundColor(Color.parseColor("#C4C4C4"))
    } else {
        tv.setBackgroundColor(Color.parseColor("#F7A74F"))
    }
}

@BindingAdapter("convert_text_color")
fun convertTextColor(tv: TextView, data: TransactionData) {
    if (data.status == "SUCCESSFUL") {
        tv.setTextColor(Color.GREEN)
    } else if (data.status == "PENDING") {
        tv.setTextColor(Color.YELLOW)
    } else if (data.status == "FAILED") {
        tv.setTextColor(Color.RED)
    }else if (data.status == "ABANDONED") {
        tv.setTextColor(Color.parseColor("#C4C4C4"))
    } else {
        tv.setTextColor(Color.parseColor("#F7A74F"))
    }
}



@BindingAdapter("convert_settlement_tag")
fun convertSettlementTag(tv: TextView, data: SettleContent) {
    if (data.settlementStatus == "SETTLED") {
        tv.setBackgroundColor(Color.GREEN)
    } else if (data.settlementStatus == "PENDING") {
        tv.setBackgroundColor(Color.YELLOW)
    } else if (data.settlementStatus == "FAILED") {
        tv.setBackgroundColor(Color.RED)
    } else {
        tv.setBackgroundColor(Color.parseColor("#F7A74F"))
    }
}