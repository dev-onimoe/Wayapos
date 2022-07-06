package com.wayapaychat.wayapay.presentation.screens.home.subscription

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CreateNewFragmentBinding
import com.wayapaychat.wayapay.databinding.CreateNewSubFragmentThreeBinding
import com.wayapaychat.wayapay.databinding.CreateNewSubFragmentTwoBinding
import com.wayapaychat.wayapay.databinding.PlansBottomSheetDialogBinding
import com.wayapaychat.wayapay.databinding.SubscriptionFragmentDialogBinding
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.PlanData
import com.wayapaychat.wayapay.framework.network.model.ViewStateSubscriptionRequest
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseBottomSheetDialog
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerDialog
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import com.wayapaychat.wayapay.presentation.screens.home.customers.CustomerAdapter
import com.wayapaychat.wayapay.presentation.screens.home.customers.CustomersEvents
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.create_link.CreatePaymentLinkFragment
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class PlansBottomSheetDialog(
    private val plans: List<PlanData>?,
    private val callBack: (PlanData) -> Unit
) :
    BaseBottomSheetDialog<PlansBottomSheetDialogBinding>(R.layout.plans_bottom_sheet_dialog) {
    private val adapter: PlanFragmentAdapter by lazy {
        PlanFragmentAdapter {
            it.let {
                callBack(it)
                dismiss()
            }
        }
    }

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        plans?.let {
            adapter.plans = it.toMutableList()
        } ?: kotlin.run {
            binding.noPlans.visibility = View.VISIBLE
        }
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}

@AndroidEntryPoint
class CreateNewFragment :
    BaseFragment<CreateNewFragmentBinding>(R.layout.create_new_fragment),
    ISelectedDate {
    val viewModel: SubscriptionViwModel by viewModels()
    private var plans: List<PlanData>? = null
    private var requestBody = ViewStateSubscriptionRequest()
    override fun init() {
        super.init()
        initView()
        viewModel.setStateEvent(SubscriptionStateEvents.GetPlans)
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        with(viewModel) {
            plansObserver.observe(
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
                        plans = it.data.data.content
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
                            message = it.error!!.message!! + it.error.details.toString(),
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
        }
    }

    private fun listeners() {
        binding.nextBtn.setOnClickListener {


            validateFields {
                requestBody.applyDateAfterFirstPayment = binding.checkbox.isChecked

                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val newdatee: Date = formatter.parse(binding.dateTxt.text.toString()) as Date
                val formatFrom = SimpleDateFormat("yyyy-MM-dd")
                val myd = formatFrom.format(newdatee)

                requestBody.startDateAfterFirstPayment = "${myd}T00:00:00.00"
                requestBody.totalCount = binding.totalCountField.text.toString().toInt()
                Navigation.findNavController(requireView())
                    .navigate(
                        CreateNewFragmentDirections.actionCreateNewFragmentToCreateNewSubFragmentTwo(
                            requestBody
                        )
                    )
            }
        }

        binding.createPlanBtn.setOnClickListener {
            CreateNewPlanDialogFragment {
                Log.d(TAG, "listeners: $it")
                it?.planId?.let {
                    requestBody.planId = it
                }
                it?.interval?.let {
                    Log.d(TAG, "listeners: $it")
                    requestBody.interval = it
                }
                it?.planName?.let {
                    requestBody.planName = it
                    binding.selectPlanField.text = it
                }
                binding.selectPlanField.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }.show(
                requireActivity().supportFragmentManager,
                "CreateCustomerDialog"
            )
        }

        binding.selectPlanField.setOnClickListener {
            PlansBottomSheetDialog(plans) {
                it.planId?.let {
                    requestBody.planId = it
                }
                it.planAmount?.let {
                    requestBody.planAmount = it
                }
                it.planName?.let {
                    requestBody.planName = it
                    binding.selectPlanField.text = it
                }
                requestBody.interval = it.interval!!
                binding.selectPlanField.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }.show(requireActivity().supportFragmentManager, "PlansBottomSheetDialog")
        }

        binding.dateCard.setOnClickListener {
            DatePickerFragment(this).show(
                requireActivity().supportFragmentManager,
                "DatePickerFragment"
            )
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (dateTxt.text == getString(R.string.dd_mm_yy)) {
                showError("Please select a date")
                return
            }
            if (selectPlanField.text == getString(R.string.select_plan)) {
                showError("Please select a plan")
                return
            }
            if (totalCountField.text.toString().isEmpty()) {
                showError("Please set total count")
                return
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {
        binding.dateTxt.text = string
    }
}

@AndroidEntryPoint
class CustomerBottomSheetDialog(
    private val plans: List<CustomerContent>?,
    private val callBack: (CustomerContent) -> Unit
) : BaseBottomSheetDialog<SubscriptionFragmentDialogBinding>(R.layout.subscription_fragment_dialog) {
    private val adapter: CustomerAdapter by lazy {
        CustomerAdapter {
            it.let {
                callBack(it)
                dismiss()
            }
        }
    }

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        plans?.let {
            adapter.customers = it.toMutableList()
        } ?: kotlin.run {
            binding.noSubscriptions.visibility = View.VISIBLE
        }
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}

@AndroidEntryPoint
class CreateNewSubFragmentTwo :
    BaseFragment<CreateNewSubFragmentTwoBinding>(R.layout.create_new_sub_fragment_two),
    ISelectedDate {
    lateinit var requestBody: ViewStateSubscriptionRequest
    val viewModel: CreateCustomerViewModel by viewModels()

    private var customers: List<CustomerContent>? = null
    override fun init() {
        super.init()
        requestBody = CreateNewSubFragmentTwoArgs.fromBundle(requireArguments()).createSubRequest
        viewModel.setStateEvents(CustomersEvents.GetCustomers,"","","",false)
        initView()
    }

    private fun initView() {
        listener()
        observers()
    }

    private fun observers() {
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
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    customers = it.data
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
                        message = it.error?.message!!,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun listener() {
        binding.nextBtn.setOnClickListener {
            validateFields {
                requestBody.notifyCustomer = binding.checkbox.isChecked
                requestBody.linkCanExpire = binding.checkbox2.isChecked
                requestBody.note = binding.noteField.text.toString()

                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val newdatee: Date = formatter.parse(binding.dateTxt.text.toString()) as Date
                val formatFrom = SimpleDateFormat("yyyy-MM-dd")
                val myd = formatFrom.format(newdatee)

                requestBody.linkExpirationDate = "${myd}T00:00:00.00"


                Log.d(TAG, "CreateNewSubFragmentTwo: $requestBody")

                Navigation.findNavController(requireView())
                    .navigate(
                        CreateNewSubFragmentTwoDirections
                            .actionCreateNewSubFragmentTwoToCreateNewSubFragmentThree(
                                requestBody
                            )
                    )
            }
        }

        binding.createCustomerBtn.setOnClickListener {
            CreateCustomerDialog {
                requestBody.customerId = it.customerId
                requestBody.customerName = "${it.firstName} ${it.lastName}"
                binding.selectCustomerField.text = "${it.firstName} ${it.lastName}"
                binding.selectCustomerField.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }.show(
                requireActivity().supportFragmentManager,
                "CreateCustomerDialog"
            )
        }

        binding.selectCustomerField.setOnClickListener {
            CustomerBottomSheetDialog(customers) {
                requestBody.customerId = it.customerId
                binding.selectCustomerField.text = "${it.firstName} ${it.lastName}"
                binding.selectCustomerField.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }.show(requireActivity().supportFragmentManager, "CustomerBottomSheetDialog")
        }
        binding.dateCard.setOnClickListener {
            DatePickerFragment(this).show(
                requireActivity().supportFragmentManager,
                "DatePickerFragment"
            )
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (selectCustomerField.text.toString() == getString(R.string.select_customer)) {
                showError("Select a customer")
                return
            }
            if (checkbox2.isChecked) {
                if (dateTxt.text.toString() == getString(R.string.dd_mm_yy)) {
                    showError("Select a date")
                    return
                }
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {

        binding.checkbox2.isChecked = true

        binding.dateTxt.text = string
    }
}

@AndroidEntryPoint
class CreateNewSubFragmentThree :
    BaseFragment<CreateNewSubFragmentThreeBinding>(R.layout.create_new_sub_fragment_three) {
    val viewModel: SubscriptionViwModel by viewModels()

    lateinit var requestBody: ViewStateSubscriptionRequest
    override fun init() {
        super.init()
        requestBody = CreateNewSubFragmentThreeArgs.fromBundle(requireArguments()).createSubRequest

        Log.d(TAG, "init: $requestBody")
        initView()
    }

    private fun initView() {
        populateField()
        listener()
        observer()
    }

    private fun observer() {
        viewModel.createSubscriptionObserver.observe(
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

                    Toast.makeText(
                        requireContext().applicationContext,
                        "Subscription Successfully Created",
                        Toast.LENGTH_SHORT
                    ).show()
                    Navigation.findNavController(requireView())
                        .popBackStack(R.id.subscriptionFragment, true)
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
                        message = it.error?.details.toString(),
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun populateField() {
        with(binding) {
            planNameValue.text = requestBody.planName
            Log.d(TAG, "populateField: ${requestBody.interval}")
            val word =
                when (CreatePaymentLinkFragment.ChargeInterval.valueOf(requestBody.interval)) {
                    CreatePaymentLinkFragment.ChargeInterval.WEEKLY -> "week"
                    CreatePaymentLinkFragment.ChargeInterval.MONTHLY -> "month"
                    CreatePaymentLinkFragment.ChargeInterval.DAILY -> "day"
                    CreatePaymentLinkFragment.ChargeInterval.ANNUALLY -> "year"
                    CreatePaymentLinkFragment.ChargeInterval.BI_ANNUAL -> "twice a year"
                    CreatePaymentLinkFragment.ChargeInterval.QUARTERLY -> "three month"
                    CreatePaymentLinkFragment.ChargeInterval.SEMI_ANNUAL -> "twice a year"
                    CreatePaymentLinkFragment.ChargeInterval.NONE -> "NONE"
                }
            val count = if (requestBody.totalCount == 0) 1 else requestBody.totalCount
            totalAmountSubTxt.text = "Subscription Amount: NGN${requestBody.planAmount} X $count"
            recurrentTxt.text = "Recurrent Payments: NGN${requestBody.planAmount}"
            billingDetailsValue.text = "Every $word the customer will be charge"
            authorizedPaymentAmountTxt.text = "Authorization Payment: NGN${requestBody.planAmount}"
            everyMonthTxt.text = "Every $word after the first payment"
            countCircle.text = "No. of cycles $count"
        }
    }

    private fun listener() {
        binding.nextBtn.setOnClickListener {
            val body = APICreateSubscriptionRequest(
                planId = requestBody.planId,
                customerId = requestBody.customerId,
                notifyCustomer = requestBody.notifyCustomer,
                applyDateAfterFirstPayment = requestBody.applyDateAfterFirstPayment,
                startDateAfterFirstPayment = requestBody.startDateAfterFirstPayment,
                linkExpirationDate = requestBody.linkExpirationDate,
                linkCanExpire = requestBody.linkCanExpire,
                totalCount = requestBody.totalCount,
                note = requestBody.note
            )

            viewModel.setStateEvent(SubscriptionStateEvents.CreateSubscription, body)
        }
    }
}
