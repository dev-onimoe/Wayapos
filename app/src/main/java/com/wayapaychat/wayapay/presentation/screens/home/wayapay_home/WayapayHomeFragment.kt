package com.wayapaychat.wayapay.presentation.screens.home.wayapay_home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.WayapayHomeFragmentBinding
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.screens.home.HomeFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.refer_earn.ReferAndEarnActivity
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionsViewModel
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class WayapayHomeFragment @Inject constructor(val myviewModel: WayaPayTransactionsViewModel) : Fragment(R.layout.wayapay_home_fragment) {

    val viewModel: WayaPayTransactionsViewModel by viewModels()
    var map = HashMap<String, Any>()

    lateinit var binding: WayapayHomeFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WayapayHomeFragmentBinding.bind(view)
        initView()
    }

    private fun initView() {
        myviewModel.setStateEvents(
            WayaPayTransactionsViewModel.TransactionsStateEvents.GetRevenueStats,
            null,"","","",""
        )
        listeners()
        Observers()

    }

    fun Observers() {
        val cache = CacheImpl(activity as Context, Gson())
        myviewModel.revenueStatsObserver.observe(viewLifecycleOwner) {
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
                    binding.grossRevenueAmount.text =
                        if (it.data.data?.revenueStats?.grossRevenue == null) "NGN 0.00" else formatToNaira(
                            it.data.data.revenueStats.grossRevenue
                        )
                    cache.putString("gross", binding.grossRevenueAmount.text as String)
                    //map.put("gross", it.data.data!!.revenueStats.grossRevenue)
                    binding.netRevenueAmount.text =
                        if (it.data.data?.revenueStats?.netRevenue == null) "NGN 0.00" else formatToNaira(
                            it.data.data.revenueStats.netRevenue
                        )
                    cache.putString("net", binding.netRevenueAmount.text as String)
                    //map.put("net", it.data.data!!.revenueStats.netRevenue)
                    binding.commissionAmount.text =
                        if (it.data.data?.revenueStats?.netRevenue == null ) "NGN 0.00" else formatToNaira(
                            it.data.data.revenueStats.grossRevenue - it.data.data.revenueStats.netRevenue
                        )
                    cache.putString("commission", binding.commissionAmount.text as String)
                    //map.put("commission", it.data.data!!.revenueStats.grossRevenue - it.data.data!!.revenueStats.netRevenue)
                    binding.lastSettlementAmount.text =
                        if (it.data.data?.settlementStats?.latestSettlement == null) "NGN 0.00" else formatToNaira(
                            it.data.data.settlementStats.latestSettlement
                        )
                    cache.putString("lastSettlement", binding.lastSettlementAmount.text as String)
                    //map.put("lastSettlement", it.data.data.settlementStats.latestSettlement)
                    binding.nextSettlementAmmount.text =
                        if (it.data.data?.settlementStats?.nextSettlement == null) "NGN 0.00" else formatToNaira(
                            it.data.data.settlementStats.nextSettlement
                        )
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
                        message = "${it.error?.message.toString()} ${it.error?.details.toString()}" ,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
        myviewModel.getTotalRevenueObserver.observe(viewLifecycleOwner) {
            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                    binding.totalRevenueValue.text = "---"

                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.toString(),
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    binding.totalRevenueValue.text = formatToNaira(it.data.data.totalRevenueForSelectedDateRange)
                    cache.putString("total", binding.totalRevenueValue.text as String)
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
                        message = "${it.error?.message.toString()} ${it.error?.details.toString()}" ,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun getJsonFromMap(map: HashMap<String, Any>): String {
        val jsonData = JSONObject()
        for (key in map.keys) {
            var value = map[key]
            if (value is HashMap<*, *>) {
                value = getJsonFromMap(value as HashMap<String, Any>)
            }
            jsonData.put(key, value)
        }
        return jsonData.toString()
    }

    private fun listeners() {
        with(binding) {
            transactionArea.setOnClickListener {
                navigateToTransactionScreen()
            }
            settlementArea.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSettlementFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
            disputeArea.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDisputeFragment()
                Navigation.findNavController(requireView()).navigate(action)
               // startActivity(Intent(requireContext().applicationContext, ComingSoonActivity::class.java))
            }
            customersArea.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToCustomersFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            paymentLinkArea.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToPaymentLinkFragment()
                Navigation.findNavController(requireView()).navigate(action)
               // startActivity(Intent(requireContext().applicationContext, ComingSoonActivity::class.java))
            }
            subscriptionArea.setOnClickListener {
               val action = HomeFragmentDirections.actionHomeFragmentToSubscriptionFragment()
                Navigation.findNavController(requireView()).navigate(action)

                //startActivity(Intent(requireContext().applicationContext, ComingSoonActivity::class.java))
            }
            referAndEarn.setOnClickListener {
                startActivity(Intent(requireContext(), ReferAndEarnActivity::class.java))
            }
        }
    }

    private fun navigateToTransactionScreen() {
        val action = HomeFragmentDirections.actionHomeFragmentToTransactionFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun loading(
        state: Boolean,
        progressBar: CircularProgressBar,
        blockUiInteraction: Boolean = false
    ) {
        if (state) {
            if (blockUiInteraction)
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            progressBar.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = View.GONE
        }
    }
}
