package com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FilterDialogBinding
import com.wayapaychat.wayapay.databinding.TotalRevenueFilterDialogBinding
import com.wayapaychat.wayapay.presentation.core.BaseBottomSheetDialog
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import java.text.SimpleDateFormat
import java.util.Date


class TotalRevenueFilterDialog(val filter: (dateFrom: String, dateTo: String) -> Unit) :
    BaseBottomSheetDialog<TotalRevenueFilterDialogBinding>(R.layout.total_revenue_filter_dialog),
    ISelectedDate {


    private var dateFrom = ""
    private var dateTo = ""


    override fun init() {
        super.init()

        binding.dateTo.setOnClickListener {
            selectDate(it as TextView)
            dateTo = it.text.toString()
        }

        binding.dateFrom.setOnClickListener {
            selectDate(it as TextView)
            dateFrom = it.text.toString()
        }

        binding.filterButton.setOnClickListener {
            filter(
                if (binding.dateFrom.text.toString() == "From") "" else binding.dateFrom.text.toString(),
                if (binding.dateTo.text.toString() == "To") "" else binding.dateTo.text.toString()
            )
            dismiss()
        }
        binding.clearButton.setOnClickListener {
            filter("", "")
            dismiss()
        }
    }

    private fun selectDate(field: TextView) {
        val selectedDateCallback = object : ISelectedDate {
            override fun onSelectedDate(string: String) {
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val newdatee: Date = formatter.parse(string) as Date
                val formatFrom = SimpleDateFormat("yyyy-MM-dd")
                val myd = formatFrom.format(newdatee)
                dateFrom = myd
                field.text = myd
            }
        }
        DatePickerFragment(selectedDateCallback)
            .show(requireActivity().supportFragmentManager, "DatePickerFragment")
    }

    override fun onSelectedDate(string: String) {

    }

    override fun onCancel(dialog: DialogInterface) {
        dismiss()
    }

}
