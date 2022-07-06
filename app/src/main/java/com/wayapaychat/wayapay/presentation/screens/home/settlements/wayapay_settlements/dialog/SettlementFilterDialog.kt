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
import com.wayapaychat.wayapay.presentation.core.BaseBottomSheetDialog
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import java.text.SimpleDateFormat
import java.util.Date


class SettlementFilterDialog(val filter: (status: String, channel: String,date: String) -> Unit) :
    BaseBottomSheetDialog<FilterDialogBinding>(R.layout.filter_dialog), ISelectedDate {

    private var selectedStatus = ""
    private var selectedChannel = ""
    private var selectedDate = ""


    override fun init() {
        super.init()

        var status = arrayOf("by Status", "select none", "SETTLED", "FAILED", "PENDING","ABANDONED")

        var channel = arrayOf("by Settlement Account","select none", "Wayapay Wallet", "CARD")

        binding.datePeriodArea.setOnClickListener {
            showDatePickerDialog()
        }

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

        val spinnerChannelAdapter = object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item, channel) {

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
                    val ew  = if (status[position] == "select none" || status[position] == "by Status") "" else status[position]
                    selectedStatus = ew
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.channelArea.apply {
            adapter = spinnerChannelAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                  val ew  = if (channel[position] == "select none" || channel[position] == "by Settlement Account") "" else channel[position]
                    selectedChannel = ew
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.filterButton.setOnClickListener {
            filter(
                if (selectedStatus == "select none") "" else selectedStatus,
                if (selectedChannel == "select none") "" else selectedChannel,
                selectedDate
            )
            dismiss()
        }
    }

    private fun showDatePickerDialog() {
        val newFragment: DialogFragment =
            DatePickerFragment(this)
        newFragment.show(this.childFragmentManager, "datePicker")
    }

    override fun onSelectedDate(string: String) {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val newdatee: Date = formatter.parse(string) as Date
        val formatFrom = SimpleDateFormat("yyyy-MM-dd")
        val myd = formatFrom.format(newdatee)

        selectedDate = myd
        binding.datePeriodArea.text = myd
    }

    override fun onCancel(dialog: DialogInterface) {
        dismiss()
    }

}
