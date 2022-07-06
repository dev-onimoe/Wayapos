package com.wayapaychat.wayapay.presentation.screens.transaction.dialog

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
import com.wayapaychat.wayapay.databinding.CustomerFilterDialogBinding
import com.wayapaychat.wayapay.databinding.FilterDialogBinding
import com.wayapaychat.wayapay.framework.network.model.Content
import com.wayapaychat.wayapay.presentation.core.BaseBottomSheetDialog
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import java.text.SimpleDateFormat
import java.util.Date

class CustomerFilterDialog(val filter: (status: String) -> Unit) :
    BaseBottomSheetDialog<CustomerFilterDialogBinding>(R.layout.customer_filter_dialog) {

    private var selectedStatus = ""

    override fun init() {
        super.init()

        var status = arrayOf("by Status","Select All", "Active", "Inactive")
        val statusSpinnerAdapter = object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item, status) {

            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup?
            ): View? {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }


        }

        binding.statusArea.apply {
            adapter = statusSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedStatus = status[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.filterButton.setOnClickListener {
            filter(
                if (selectedStatus == "Select All") "" else selectedStatus
            )
            dismiss()
        }
    }

}
