package com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details

import android.Manifest
import android.R.attr.label import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CustomerSubsriptionFragBinding

import com.wayapaychat.wayapay.databinding.CustomerTransactionFragBinding

import com.wayapaychat.wayapay.databinding.ViewCustomerSubscriptionBinding
import com.wayapaychat.wayapay.databinding.PaymentItemBinding
import com.wayapaychat.wayapay.databinding.CustomerDetailFragBinding
import com.wayapaychat.wayapay.framework.network.model.CustomerData2
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponseContent
import com.wayapaychat.wayapay.framework.network.model.PlanData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import com.wayapaychat.wayapay.presentation.screens.home.customers.CustomersEvents
import com.wayapaychat.wayapay.presentation.screens.transaction.TransactionFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.adapter.TransactionAdapter
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomerDetailFrag(private val customerData: CustomerData2?) :
    Fragment(R.layout.customer_detail_frag) {

    val REQUEST_PHONE_CALL = 1

    lateinit var binding: CustomerDetailFragBinding

    var toggle = customerData?.customerAvoided

    val viewModel: CreateCustomerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CustomerDetailFragBinding.bind(view)
        initView()
    }

    private fun initView() {
        updateView()
        listener()
        observer()
    }

    private fun observer() {
        viewModel.avoidCustomerObserver.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is StateMachine.Loading -> {
                    Toast.makeText(requireContext(), "Loading....", Toast.LENGTH_SHORT).show()
                }

                is StateMachine.Error -> {
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "ok"
                    )
                }

                is StateMachine.Success -> {
                    showAlertDialogMessage(
                        message = it.data.message,
                        positiveBottomText = "Ok"
                    )
                }

                is StateMachine.TimeOut -> {
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Ok"
                    )
                }
                is StateMachine.GenericError -> {
                    showAlertDialogMessage(
                        message = it.error!!.message!!,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun listener() {
        with(binding) {
            call.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + customerData?.phoneNumber)

                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_PHONE_CALL
                    );
                } else {
                    startActivity(callIntent)
                }
            }

            avoidThisUserToggle.setOnClickListener {

                toggle = !toggle!!
                viewModel.setStateEvents(
                    CustomersEvents.ToggleCustomer, customerData?.customerId, "", "",
                    toggle!!
                )

            }
            customerIdValue.setOnClickListener {
                val intent = ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setText(customerData?.customerId)
                    .intent
                startActivity(intent)
            }

        }
    }

    private fun updateView() {
        with(binding) {
            phoneValue.text = customerData?.phoneNumber
            addedDateValue.text = customerData?.createdAt
            customerIdValue.text = customerData?.customerId
            avoidThisUserToggle.isChecked = customerData?.customerAvoided ?: false
        }
    }
}

class CustomerSubscriptionFrag() : Fragment(R.layout.customer_subsription_frag) {

    lateinit var binding: CustomerSubsriptionFragBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CustomerSubsriptionFragBinding.bind(view)
    }
}

@AndroidEntryPoint
class CustomerTransactionFrag @Inject constructor (private val customerId: String?) :
    Fragment(R.layout.customer_transaction_frag) {

    private val adapter: CustomerTransactionsAdapter by lazy {
        CustomerTransactionsAdapter()
    }


    lateinit var binding: CustomerTransactionFragBinding

    val viewModel: CreateCustomerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CustomerTransactionFragBinding.bind(view)

        viewModel.setStateEvents(
            CustomersEvents.GetCustomerTransactions,
            requestBody = customerId,
            "",
            "",
            false
        )

        initView()
    }


    private fun initView() {
        setUpRecyclerview()
        listener()
        observer()
    }

    private fun observer() {
        viewModel.customerTransactionsObserver.observe(viewLifecycleOwner) {
            when (it) {
                is StateMachine.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }

                is StateMachine.Error -> {
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {

                    if (it.data.data.content.isEmpty()) {
                       // Toast.makeText(requireContext(), "No transactions", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        adapter.transactions = it.data.data.content as MutableList<CustomerTransactionApiResponseContent>
                    }

                }

                is StateMachine.TimeOut -> {
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.GenericError -> {
                    showAlertDialogMessage(
                        message = it.error!!.message!!.toString(),
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }

    }

    private fun listener() {

    }

    private fun setUpRecyclerview() {
        binding.recyclerView.apply {
            adapter = this@CustomerTransactionFrag.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}

class CustomerTransactionsAdapter() : BaseAdapter() {
    var transactions = mutableListOf<CustomerTransactionApiResponseContent>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val transactionsData = transactions[position]
        val planBinding = binding as PaymentItemBinding
        planBinding.linkName.text = transactionsData.amount.toString()
        planBinding.amount.text = transactionsData.customerName
        planBinding.dateTime.text = transactionsData.tranDate
    }

    override fun getItemCount() = transactions.size
}

class ViewCustomerSubscription() :
    BaseFragment<ViewCustomerSubscriptionBinding>(R.layout.view_customer_subscription)
