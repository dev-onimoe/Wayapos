package com.wayapaychat.wayapay.presentation.screens.home.dispute.dispute_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.AcceptChargebackDialogBinding
import com.wayapaychat.wayapay.databinding.CustomerDetailFragBinding
import com.wayapaychat.wayapay.databinding.FragmentAcceptChargeBackBinding
import com.wayapaychat.wayapay.databinding.SuccessAcceptChargebackDialogBinding
import com.wayapaychat.wayapay.databinding.WelcomeDialogBinding
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerData2
import com.wayapaychat.wayapay.framework.network.model.GetAllDisputeResponseDataContent
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import com.wayapaychat.wayapay.presentation.screens.home.dispute.DisputeFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.home.dispute.WayaPayDisputeViewModel
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AcceptChargeBackFragment(private val amount: Double,val disputeID : String) : Fragment(R.layout.fragment_accept_charge_back) {


    lateinit var binding: FragmentAcceptChargeBackBinding

    val viewModel: WayaPayDisputeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptChargeBackBinding.bind(view)
        binding.amountField.setText("Full Refund (NGN ${amount})")

        observer()
        binding.acceptChargeBackBtn.setOnClickListener {
           val dailog = AcceptChargeBackDialogueFragment(
                accept = {
               val data = AcceptChargeBackRequest(binding.commentOption.text.toString(),amount.toInt())
               viewModel.acceptChargeBack(disputeID,data) })

            dailog.show(requireActivity().supportFragmentManager, "WelcomeDialogFragment")
            dailog.isCancelable = false
        }
    }

    private fun observer() {
        viewModel.acceptChargeBackObserver .observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is StateMachine.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is StateMachine.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "ok"
                    )
                }

                is StateMachine.Success -> {
                     binding.progressBar.visibility = View.GONE

                    val dailog = SucccesAcceptChargeBackDialogueFragment(
                        message = "Your have successfully accepted chargeback and such lost this dispite. Your account will be debited shortly.",
                        goHome = {
                            Navigation.findNavController(requireView()).popBackStack()
                        }
                    )

                    dailog.show(requireActivity().supportFragmentManager, "WelcomeDialogFragment")
                    dailog.isCancelable = false

                }

                is StateMachine.TimeOut -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Ok"
                    )
                }
                is StateMachine.GenericError -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialogMessage(
                        message = it.error?.data.toString(),
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

}


class SucccesAcceptChargeBackDialogueFragment(
    private val goHome: () -> Unit,
    private val message : String
) : BaseDialogFragment(R.layout.success_accept_chargeback_dialog) {

    private lateinit var binding: SuccessAcceptChargebackDialogBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SuccessAcceptChargebackDialogBinding.bind(view)
        initView()
    }

    private fun initView() {
        binding.messageTxt.text = message
        listeners()
    }

    private fun listeners() {
        with(binding) {
            goHome.setOnClickListener {
                goHome()
                dismiss()
            }
        }
    }
}


class AcceptChargeBackDialogueFragment(
    private val accept: () -> Unit,
) : BaseDialogFragment(R.layout.accept_chargeback_dialog) {

    private lateinit var binding: AcceptChargebackDialogBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AcceptChargebackDialogBinding.bind(view)
        initView()
    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            acceptBtn.setOnClickListener {
                accept()
                dismiss()
            }
            cancel.setOnClickListener {
                dismiss()
            }
        }
    }
}
