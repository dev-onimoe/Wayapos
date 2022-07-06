package com.wayapaychat.wayapay.presentation.screens.home.customers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CustomersFragmentBinding
import com.wayapaychat.wayapay.databinding.PaymentItemBinding
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.dialog.CustomerFilterDialog
import com.wayapaychat.wayapay.presentation.screens.transaction.dialog.FilterDialog
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomersFragment : BaseFragment<CustomersFragmentBinding>(R.layout.customers_fragment) {

    var status: String = ""
    var search: String = ""

    private val adapter by lazy {
        CustomerAdapter { customer ->
            Navigation.findNavController(requireView())
                .navigate(
                    CustomersFragmentDirections
                        .actionCustomersFragmentToCustomerDetailsFragment2(customer.customerId)
                )
        }
    }
    val viewModel: CreateCustomerViewModel by viewModels()
    override fun init() {
        super.init()
        initView()
        viewModel.setStateEvents(CustomersEvents.GetCustomers,"",status, search,false)
    }

    private fun initView() {
        listeners()
        observer()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observer() {
        viewModel.customersObserver.observe(
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
                    if (it.data.isNullOrEmpty()){
                        binding.progressBar.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                        binding.recyclerView .visibility = View.GONE
                        adapter.customers = emptyList()
                    }else{
                        binding.recyclerView .visibility = View.VISIBLE
                        binding.emptyText.visibility = View.GONE
                        adapter.customers = it.data
                    }
                }

                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Ok"
                    )
                }
                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
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

    private fun listeners() {
        with(binding) {
            fab.setOnClickListener {
                CreateCustomerDialog {
                    viewModel.setStateEvents(CustomersEvents.GetCustomers,"",status, search,false)
                }.show(
                    requireActivity().supportFragmentManager,
                    "CreateCustomerDialog"
                )
            }

            searchBar.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setStateEvents(CustomersEvents.GetCustomers,"",status, s.toString(),false)
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            filterButton.setOnClickListener {
                CustomerFilterDialog { status ->
                    viewModel.setStateEvents(CustomersEvents.GetCustomers,"",status, search,false)
                }.show(requireActivity().supportFragmentManager, "Transaction_Fragment")
            }

        }
    }
}

class CustomerAdapter(private val listener: (CustomerContent) -> Unit) : BaseAdapter() {
    var customers = listOf<CustomerContent>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val content = customers[position]
        val customerItemBinding = binding as PaymentItemBinding

        customerItemBinding.amount.text = content.email
        customerItemBinding.linkName.text = content.lastName + " " + content.firstName
        customerItemBinding.dateTime.text = content.createdAt.substring(0,10)

        binding.root.setOnClickListener {
            listener(content)
        }
    }

    override fun getItemCount() = customers.size
}
