package com.wayapaychat.wayapay.presentation.screens.home.subscription.plan

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FilterPlanDialogLayoutBinding
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.collections.ArrayList

data class FilterData(
    val status: String,
    val cardExpiringIn: String,
    val startDate: String,
    val endDate: String
)

@AndroidEntryPoint
class FilterPlanDialog(private val filter: (FilterData) -> Unit) :
    BaseDialogFragment(R.layout.filter_plan_dialog_layout) {
    val viewModel: CreateCustomerViewModel by viewModels()
    private var isCustomDate = false

    enum class Status {
        SHOW_ALL, CREATED, ACTIVE, ACTIVE_RENEWING, ACTIVE_NON_RENEWING, INACTIVE, ATTENTION, CANCEL
    }

    private val statuses = arrayOf(
        "Show All",
        "Created",
        "Active",
        "Active(Renewing)",
        "Active(Non-renewing)",
        "Inactive",
        "Attention",
        "Cancel"
    )
    private val cardExpires = arrayOf(
        "All Time",
        "Next 7 Days",
        "Next 30 Days",
        "Next 60 Days",
        "Specific Period"
    )
    private lateinit var binding: FilterPlanDialogLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FilterPlanDialogLayoutBinding.bind(view)
        initView()
    }

    private fun initView() {
        listener()
    }

    private fun listener() {
        with(binding) {
            applyFilterBtn.setOnClickListener {
                validateField {
                    validateDate {
                        val data = FilterData(
                            status = selectedStatus,
                            cardExpiringIn = cardStartDate.text.toString().trim(),
                            startDate = fromDate.text.toString(),
                            endDate = toDate.text.toString()
                        )
                        filter(data)
                        dismiss()
                    }
                }
            }
            clearFilterBtn.setOnClickListener {
                fromDate.text = getString(R.string.start_date)
                toDate.text = getString(R.string.end_date)
                cardStartDate.text = getString(R.string.choose_date)
            }
            cardStartDate.setOnClickListener {
                selectDate(it as TextView)
            }
            toDate.setOnClickListener {
                selectDate(it as TextView)
            }

            fromDate.setOnClickListener {
                selectDate(it as TextView)
            }

            val statusAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    statuses
                )
            binding.statusField.apply {
                adapter = statusAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedStatus = statuses[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }

            val cardAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    cardExpires
                )
            binding.cardField.apply {
                adapter = cardAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedCardFilter = cardExpires[position]
                        if (position == cardExpires.size - 1) {
                            isCustomDate = true
                            specificCard.visibility = View.VISIBLE
                            cardStartDate.visibility = View.VISIBLE
                        } else {
                            isCustomDate = false
                            specificCard.visibility = View.GONE
                            cardStartDate.visibility = View.GONE
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private lateinit var selectedStatus: String
    private lateinit var selectedCardFilter: String

    private fun validateDate(callback: () -> Unit) {
        val dates = getDates(binding.fromDate.text.toString(), binding.toDate.text.toString())
        val newDates = ArrayList<String>()
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        dates.forEach {
            newDates.add(df1.format(it))
        }
        if (newDates.isEmpty()) {
            showError("Start Date can't be ahead of End Date")
            return
        }

        callback()
    }

    private fun getDates(dateString1: String, dateString2: String): List<Date> {
        val dates = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    private fun selectDate(field: TextView) {
        val selectedDateCallback = object : ISelectedDate {
            override fun onSelectedDate(string: String) {
                field.text = string
            }
        }
        DatePickerFragment(selectedDateCallback)
            .show(requireActivity().supportFragmentManager, "DatePickerFragment")
    }

    private fun validateField(callback: () -> Unit) {
        with(binding) {
            if (selectedCardFilter == cardExpires[4]) {
                if (cardStartDate.text.toString() == getString(R.string.choose_date)) {
                    showError("please select card date")
                    return
                }
            }
            if (fromDate.text.toString() == getString(R.string.start_date)) {
                showError("please select a start date")
                return
            }

            if (toDate.text.toString() == getString(R.string.end_date)) {
                showError("please select an end date")
                return
            }
        }
        callback()
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        requireDialog().window?.setLayout(width, height)
    }
}
