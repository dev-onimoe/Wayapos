package com.wayapaychat.wayapay.presentation.screens.home.dispute

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.DisputeFragmentBinding
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.HomeFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.home.dispute.adapter.DisputeAdapter
import com.wayapaychat.wayapay.presentation.screens.home.settlements.SettlementFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.WayaPaySettlementsViewModel
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.adapter.SettlementAdapter
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dispute_details.appBar

@AndroidEntryPoint
class DisputeFragment : BaseFragment<DisputeFragmentBinding>(R.layout.dispute_fragment) {

    val viewModel: WayaPayDisputeViewModel by viewModels()


    private val adapter: DisputeAdapter by lazy {
        DisputeAdapter { dispute_data ->

            if (dispute_data.accepted) {
                Navigation.findNavController(requireView()).navigate(
                    DisputeFragmentDirections.actionDisputeFragmentToResolvedDisputeFragment(
                        dispute_data
                    )
                )
            } else {
                Navigation.findNavController(requireView()).navigate(
                    DisputeFragmentDirections.actionDisputeFragmentToDisputeDetailsFragment(dispute_data)
                )
            }
        }
    }

    override fun init() {
        initView()
    }

    private fun initView() {
        setUpRecyclerview()
        observer()
    }

    private fun observer() {
        viewModel.allDisputeObserver.observe(this) {
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
                    if (it.data.data.content == null || it.data.data.content.isEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        adapter.disputes = emptyList()
                    } else {
                        adapter.disputes = it.data.data.content
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
            adapter = this@DisputeFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}