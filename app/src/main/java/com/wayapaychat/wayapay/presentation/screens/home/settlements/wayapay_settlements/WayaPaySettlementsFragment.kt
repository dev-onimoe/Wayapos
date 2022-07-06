package com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentWayaPaySettlementsBinding
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.SettlementContent
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.settlements.SettlementFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.adapter.SettlementAdapter
import com.wayapaychat.wayapay.presentation.screens.transaction.TransactionFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionsViewModel
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.adapter.TransactionAdapter
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WayaPaySettlementsFragment @Inject constructor() :
    BaseFragment<FragmentWayaPaySettlementsBinding>(R.layout.fragment_waya_pay_settlements) {

    val viewModel: WayaPaySettlementsViewModel by viewModels()


    private val adapter: SettlementAdapter by lazy {
        SettlementAdapter { settlement_data ->
            Navigation.findNavController(requireView()).navigate(
            SettlementFragmentDirections.actionSettlementFragmentToViewSettlementFragment(settlement_data)
            )
        }
    }

    override fun init() {
        super.init()

        val status = requireArguments().getString("status")
        val channel = requireArguments().getString("channel")
        val date = requireArguments().getString("date")
        val search = requireArguments().getString("search")


        viewModel.setStateEvents(
            WayaPaySettlementsViewModel.SettlementsStateEvents.GetAllSettlements,
            "",
            status!!,
            channel!!,
            date!!,
            search!!
        )
        initView()
    }


    private fun observer() {
        viewModel.allSettlementsObserver.observe(this) {
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
                    if (it.data == null|| it.data.isEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                        binding.recyclerView .visibility = View.GONE

                        adapter.settlements = emptyList()
                    } else {
                        adapter.settlements = it.data
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


    private fun initView() {
        setUpRecyclerview()
        observer()
    }


    private fun setUpRecyclerview() {
        binding.recyclerView.apply {
            adapter = this@WayaPaySettlementsFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


}