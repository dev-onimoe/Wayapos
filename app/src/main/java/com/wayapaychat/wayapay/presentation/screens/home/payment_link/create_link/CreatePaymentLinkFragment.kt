package com.wayapaychat.wayapay.presentation.screens.home.payment_link.create_link

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ChoosePaymentLinkBinding
import com.wayapaychat.wayapay.databinding.CreatePaymentLinkBinding
import com.wayapaychat.wayapay.databinding.CreateRecurrentPaymentLinkBinding
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.CreatePaymentEvents
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.CreatePaymentLinkViewmodel
import com.wayapaychat.wayapay.presentation.utils.ext.views.removeCommas
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePaymentLinkFragment :
    BaseFragment<CreatePaymentLinkBinding>(R.layout.create_payment_link) {

    val viewModel: CreatePaymentLinkViewmodel by viewModels()

    enum class PaymentLinkType {
        ONE_TIME_PAYMENT_LINK, SUBSCRIPTION_PAYMENT_LINK,CUSTOMER_SUBSCRIPTION_PAYMENT_LINK
    }

    enum class ChargeInterval {
        WEEKLY, MONTHLY, DAILY, ANNUALLY, BI_ANNUAL, QUARTERLY, SEMI_ANNUAL, NONE
    }

    private lateinit var chargeInterval: ChargeInterval
    private lateinit var chargeIntervals: Array<ChargeInterval>

    private lateinit var intervals: Array<String>

    private lateinit var paymentLinkType: PaymentLinkType
    override fun init() {
        super.init()
        chargeIntervals = arrayOf(
            ChargeInterval.NONE,
            ChargeInterval.WEEKLY,
            ChargeInterval.MONTHLY,
            ChargeInterval.DAILY,
            ChargeInterval.ANNUALLY,
            ChargeInterval.BI_ANNUAL,
            ChargeInterval.QUARTERLY,
            ChargeInterval.SEMI_ANNUAL
        )
        intervals = arrayOf(
            getString(R.string.choose_plan_interval),
            ChargeInterval.WEEKLY.name,
            ChargeInterval.MONTHLY.name,
            ChargeInterval.DAILY.name,
            ChargeInterval.ANNUALLY.name,
            ChargeInterval.BI_ANNUAL.name.replace("_", " "),
            ChargeInterval.QUARTERLY.name,
            ChargeInterval.SEMI_ANNUAL.name.replace("_", " ")
        )
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        viewModel.createPaymentLinkObserver.observe(
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
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    Navigation.findNavController(requireView())
                        .popBackStack()

                    showAlertDialogMessage(
                        message = "payment Link Successfully Created",
                        positiveBottomText = "Done"
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
            createLinkBtn.setOnClickListener {
                validateFields {
                    val requestBody = APICreatePaymentLink(
                        paymentLinkType = paymentLinkType.name,
                        paymentLinkName = binding.paymentLinkField.text.toString(),
                        description = binding.descriptionTxtField.text.toString(),
                        payableAmount = binding.amountField.text.toString().removeCommas(),
                        chargeInterval = chargeInterval.name,
                        totalCount = binding.countField.text.toString().toInt()
                    )
                    viewModel.setStatEvents(
                        CreatePaymentEvents.CreatePaymentLink,
                        requestBody
                    )
                }
            }
            cancelButton.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }
            back.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }

            val arrayAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    intervals
                )
            binding.intervalSpinner.apply {
                adapter = arrayAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        chargeInterval = chargeIntervals[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun validateFields(callback: () -> Unit) {
        if (binding.paymentLinkField.text.toString().isEmpty()) {
            showError("Please name your payment link")
            return
        }

        if (binding.amountField.text.toString().isEmpty()) {
            showError("Please set an amount")
            return
        }

        if (chargeInterval == ChargeInterval.NONE) {
            showError("Please set an Interval")
            return
        }
        if (binding.countField.text.toString().isEmpty()) {
            showError("Please set count")
            return
        }

        if (binding.phoneField.text.toString().isEmpty()) {
            showError("Please set phone number")
            return
        }
        callback()
    }
}



@AndroidEntryPoint
class CreateRecurrentPaymentLinkFragment :
    BaseFragment<CreateRecurrentPaymentLinkBinding>(R.layout.create_recurrent_payment_link) {

    val viewModel: CreatePaymentLinkViewmodel by viewModels()

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        viewModel.createPaymentLinkObserver.observe(
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
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    Navigation.findNavController(requireView())
                        .popBackStack()

                    showAlertDialogMessage(
                        message = "payment Link Successfully Created",
                        positiveBottomText = "Done"
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
            createLinkBtn.setOnClickListener {
                validateFields {
                    val requestBody = APICreatePaymentLink(
                        paymentLinkType = "SUBSCRIPTION_PAYMENT_LINK",
                        paymentLinkName = binding.paymentLinkField.text.toString(),
                        description = binding.descriptionTxtField.text.toString(),
                        payableAmount = binding.amountField.text.toString().removeCommas(),
                        chargeInterval = "MONTHLY",
                        totalCount = 0
                    )
                    viewModel.setStatEvents(
                        CreatePaymentEvents.CreatePaymentLink,
                        requestBody
                    )
                }
            }
            cancelButton.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }
            back.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }

        }
    }

    private fun validateFields(callback: () -> Unit) {
        if (binding.paymentLinkField.text.toString().isEmpty()) {
            showError("Please name your payment link")
            return
        }

        if (binding.amountField.text.toString().isEmpty()) {
            showError("Please set an amount")
            return
        }

        if (binding.phoneField.text.toString().isEmpty()) {
            showError("Please set phone number")
            return
        }
        callback()
    }
}


class ChooseLinkTypeDialog(private val callback: (CreatePaymentLinkFragment.PaymentLinkType) -> Unit) :
    BaseDialogFragment(R.layout.choose_payment_link) {

    private lateinit var binding: ChoosePaymentLinkBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChoosePaymentLinkBinding.bind(view)
        initView()
    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            chooseSubBtn.setOnClickListener {
                callback(CreatePaymentLinkFragment.PaymentLinkType.SUBSCRIPTION_PAYMENT_LINK)
                dismiss()
            }
            chooseOneTime.setOnClickListener {
                callback(CreatePaymentLinkFragment.PaymentLinkType.ONE_TIME_PAYMENT_LINK)
                dismiss()
            }
        }
    }
}
