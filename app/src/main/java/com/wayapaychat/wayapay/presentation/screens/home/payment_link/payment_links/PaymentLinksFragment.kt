package com.wayapaychat.wayapay.presentation.screens.home.payment_link.payment_links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FilterPaymentLinkDialogLayoutBinding
import com.wayapaychat.wayapay.databinding.PaymentItemBinding
import com.wayapaychat.wayapay.databinding.PaymentLinkFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.Content
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.create_link.ChooseLinkTypeDialog
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.CreatePaymentEvents
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.create_link.CreatePaymentLinkFragment
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.CreatePaymentLinkViewmodel
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PaymentLinksFragment :
    BaseFragment<PaymentLinkFragmentBinding>(R.layout.payment_link_fragment) {
    val viewModel: CreatePaymentLinkViewmodel by viewModels()
    private val adapter: PaymentLinkAdapter by lazy {
        PaymentLinkAdapter {

            val paymentLinkType =
                CreatePaymentLinkFragment.PaymentLinkType.valueOf(it.paymentLinkType)

            if (paymentLinkType == CreatePaymentLinkFragment.PaymentLinkType.ONE_TIME_PAYMENT_LINK)
                Navigation.findNavController(requireView()).navigate(
                    PaymentLinksFragmentDirections.actionPaymentLinksFragmentToViewOneTimePaymentLinkFragment(
                        it
                    )
                )
            else {
                Toast.makeText(requireContext(), "Coming Soon!", Toast.LENGTH_SHORT).show()
               /* Navigation.findNavController(requireView()).navigate(
                    PaymentLinksFragmentDirections.actionPaymentLinksFragmentToSubscriptionDetailsFragment(
                        it
                    )
                )*/
            }
        }
    }

    override fun init() {
        super.init()
        initView()
        viewModel.setStatEvents(CreatePaymentEvents.PaymentLinks)
    }

    private fun initView() {
        listeners()
        setUpRecyclerView()
        observers()
    }

    private fun observers() {
        viewModel.paymentLinkObserver.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "ok"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    if (it.data.data.content.isEmpty()) {
                        binding.noRecordFound.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noRecordFound.visibility = View.INVISIBLE
                        adapter.paymentLinkItems = it.data.data.content.reversed().toMutableList()
                    }

                }
                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "ok"
                    )
                }
                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = "${it.error?.message.toString()} ${it.error?.details.toString()}",
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun listeners() {
        with(binding) {
            fab.setOnClickListener {
                ChooseLinkTypeDialog {
                    when (it) {
                        CreatePaymentLinkFragment.PaymentLinkType.ONE_TIME_PAYMENT_LINK -> {
                            Navigation.findNavController(requireView())
                                .navigate(
                                    PaymentLinksFragmentDirections.actionPaymentLinksFragmentToCreateRecurrentPaymentLinkFragment()
                                )
                        }
                        else -> {
                            Navigation.findNavController(requireView())
                                .navigate(
                                    PaymentLinksFragmentDirections.actionPaymentLinksFragmentToCreatePaymentLinkFragment()
                                )
                        }
                    }

                }.show(requireActivity().supportFragmentManager, "ChooseLinkTypeDialog")
            }

            filterButton.setOnClickListener {
                FilterPaymentLinksDialog { data ->
                    viewModel.setStatEvents(
                        CreatePaymentEvents.PaymentLinks,
                        null,
                        data.startDate,
                        data.status.uppercase(Locale.ROOT),
                        data.endDate
                    )

                }.show(
                    requireActivity().supportFragmentManager, "FilterPaymentLinksDialog"
                )
            }
        }
    }
}

data class FilterPaymentData(
    val status: String,
    val startDate: String,
    val endDate: String
)

@AndroidEntryPoint
class FilterPaymentLinksDialog(private val filter: (FilterPaymentData) -> Unit) :
    BaseDialogFragment(R.layout.filter_payment_link_dialog_layout) {
    val viewModel: CreateCustomerViewModel by viewModels()
    private val statuses = arrayOf(
        "Active",
        "Inactive"
    )

    private lateinit var binding: FilterPaymentLinkDialogLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FilterPaymentLinkDialogLayoutBinding.bind(view)
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

                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        val newdate: Date = formatter.parse(toDate.text.toString()) as Date
                        val formatFrom = SimpleDateFormat("MM-dd-yyyy")
                        val myStartDate = formatFrom.format(newdate)

                        val newdate2: Date = formatter.parse(fromDate.text.toString()) as Date
                        val myEndDate = formatFrom.format(newdate2)

                        val filterPaymentData = FilterPaymentData(
                            status = selectedStatus,
                            startDate = myStartDate,
                            endDate = myEndDate
                        )
                        filter(filterPaymentData)
                        dismiss()
                    }
                }
            }
            clearFilterBtn.setOnClickListener {
                fromDate.text = getString(R.string.start_date)
                toDate.text = getString(R.string.end_date)

                val filterPaymentData = FilterPaymentData(
                    status = "",
                    startDate = "",
                    endDate = ""
                )
                filter(filterPaymentData)
                dismiss()

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
        }
    }

    private lateinit var selectedStatus: String

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

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date1: Date = formatter.parse(dateString1) as Date
        val date2: Date = formatter.parse(dateString2) as Date

        dates.add(date1)
        dates.add(date2)

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
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        requireDialog().window?.setLayout(width, height)
    }
}

class PaymentLinkAdapter(private val listener: (Content) -> Unit) : BaseAdapter() {
    var paymentLinkItems = mutableListOf<Content>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val paymentLinkBinding = binding as PaymentItemBinding
        val paymentLinkItem = paymentLinkItems[position]
        paymentLinkBinding.amount.text = "NGN${paymentLinkItem.payableAmount}"
        paymentLinkBinding.linkName.text = paymentLinkItem.paymentLinkName
        paymentLinkBinding.dateTime.text = paymentLinkItem.createdAt.substring(0, 10)
        paymentLinkBinding.subType.text = if (
            CreatePaymentLinkFragment.PaymentLinkType.valueOf(paymentLinkItem.paymentLinkType)
            ==
            CreatePaymentLinkFragment.PaymentLinkType.ONE_TIME_PAYMENT_LINK
        ) "One Time" else "Subscription"

        binding.root.setOnClickListener {
            listener(paymentLinkItem)
        }
    }

    override fun getItemCount() = paymentLinkItems.size
}
