package com.wayapaychat.wayapay.presentation.screens.transaction.wayapay

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.WayapayTransactionBinding
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.TransactionFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionsViewModel.TransactionsStateEvents.GetAllTransactions
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.adapter.SettlementAdapter
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.adapter.TransactionAdapter
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WayaPayTransactionFragment @Inject constructor() :
    BaseFragment<WayapayTransactionBinding>(R.layout.wayapay_transaction) {

    val viewModel: WayaPayTransactionsViewModel by viewModels()

    @Inject
    lateinit var cache: Cache

    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter { transaction_data ->
            Navigation.findNavController(requireView())
                .navigate(
                    TransactionFragmentDirections.actionTransactionFragmentToViewTransactionFragment(
                        transaction_data
                    )
            )
        }
    }

    override fun init() {
        super.init()

        val status = requireArguments().getString("status")
        val channel = requireArguments().getString("channel")
        val date = requireArguments().getString("date")
        val search = requireArguments().getString("search")

        val loginResponse =
            cache.getObject(
                CacheConstants.Keys.USER_DETAILS,
                APILoginResponse::class.java
            ) as APILoginResponse

        viewModel.setStateEvents(
            GetAllTransactions,
            loginResponse.data?.merchantId.toString(),
            status!!,
            channel!!,
            date!!,
            search!!
        )
        initView()
    }

    private fun initView() {
        setUpRecyclerview()
        observer()
    }

    private fun observer() {
        viewModel.allTransactionsObserver.observe(this) {
            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    if (it.data == null || it.data.isEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                        binding.recyclerView .visibility = View.GONE
                        adapter.transactions = emptyList()
                    } else {
                        adapter.transactions = it.data
                    }
                }

                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
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

    private fun setUpRecyclerview() {
        binding.recyclerView.apply {
            adapter = this@WayaPayTransactionFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}
