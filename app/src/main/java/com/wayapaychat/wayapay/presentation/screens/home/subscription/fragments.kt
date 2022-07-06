package com.wayapaychat.wayapay.presentation.screens.home.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CreateNewPlanDialogBinding
import com.wayapaychat.wayapay.databinding.PaymentItemBinding
import com.wayapaychat.wayapay.databinding.PlanFragmentLayoutBinding
import com.wayapaychat.wayapay.databinding.SubFragmentLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerSubscriptionData
import com.wayapaychat.wayapay.framework.network.model.PlanData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.screens.home.payment_link.create_link.CreatePaymentLinkFragment
import com.wayapaychat.wayapay.presentation.screens.home.subscription.plan.FilterPlanDialog
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint

class SubFragmentAdapter(private val listener: (CustomerSubscriptionData) -> Unit) : BaseAdapter() {
    var subscriptions = mutableListOf<CustomerSubscriptionData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val subscriptionData = subscriptions[position]
        val subscriptionBinding = binding as PaymentItemBinding

        subscriptionBinding.linkName.text = subscriptionData.planId
        subscriptionBinding.amount.text = ""

        subscriptionBinding.dateTime.text = subscriptionData.createdAt?.substring(0,9)

        binding.root.setOnClickListener {
            listener(subscriptionData)
        }
    }

    override fun getItemCount() = subscriptions.size
}

class PlanFragmentAdapter(private val listener: (PlanData) -> Unit) : BaseAdapter() {
    var plans = mutableListOf<PlanData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val planData = plans[position]
        val planBinding = binding as PaymentItemBinding
        planBinding.linkName.text = planData.planName
        planBinding.amount.text = planData.planId
        planBinding.dateTime.text = planData.createdAt?.substring(0,9)
        binding.root.setOnClickListener {
            listener(planData)
        }
    }

    override fun getItemCount() = plans.size
}

class SubViewPager(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            SubFragment()
        } else {
            PlanFragment()
        }
    }
}

@AndroidEntryPoint
class SubFragment : Fragment(R.layout.sub_fragment_layout) {
    lateinit var binding: SubFragmentLayoutBinding
    val viewModel: SubscriptionViwModel by viewModels()
    private val adapter: SubFragmentAdapter by lazy {
        SubFragmentAdapter {
            Navigation.findNavController(requireView())
                .navigate(
                    SubscriptionFragmentDirections
                        .actionSubscriptionFragmentToViewSubscriptionFragment(it)
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SubFragmentLayoutBinding.bind(view)
        initView()
        viewModel.setStateEvent(SubscriptionStateEvents.GetSubscriptions)
    }

    private fun initView() {
        observer()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observer() {
        with(viewModel) {
            subscriptionsObserver.observe(
                viewLifecycleOwner,
                {
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
                            adapter.subscriptions = it.data.data.content.toMutableList()
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
            )
        }
    }

    fun loading(state: Boolean, progressBar: CircularProgressBar) {
        if (state) {
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

    fun loadTransactions() {
        //  viewModel.setStateEvent(SubscriptionStateEvents.GetSubscriptions)
    }
}

@AndroidEntryPoint
class CreateNewPlanDialogFragment(private val refresh: (PlanData?) -> Unit) :
    BaseDialogFragment(R.layout.create_new_plan_dialog) {
    val viewModel: SubscriptionViwModel by viewModels()
    private lateinit var chargeIntervals: Array<CreatePaymentLinkFragment.ChargeInterval>

    private lateinit var intervals: Array<String>
    private lateinit var interval: CreatePaymentLinkFragment.ChargeInterval
    private lateinit var binding: CreateNewPlanDialogBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateNewPlanDialogBinding.bind(view)
        chargeIntervals = arrayOf(
            CreatePaymentLinkFragment.ChargeInterval.NONE,
            CreatePaymentLinkFragment.ChargeInterval.WEEKLY,
            CreatePaymentLinkFragment.ChargeInterval.MONTHLY,
            CreatePaymentLinkFragment.ChargeInterval.DAILY,
            CreatePaymentLinkFragment.ChargeInterval.ANNUALLY,
            CreatePaymentLinkFragment.ChargeInterval.BI_ANNUAL,
            CreatePaymentLinkFragment.ChargeInterval.QUARTERLY,
            CreatePaymentLinkFragment.ChargeInterval.SEMI_ANNUAL
        )
        intervals = arrayOf(
            getString(R.string.choose_plan_interval),
            CreatePaymentLinkFragment.ChargeInterval.WEEKLY.name,
            CreatePaymentLinkFragment.ChargeInterval.MONTHLY.name,
            CreatePaymentLinkFragment.ChargeInterval.DAILY.name,
            CreatePaymentLinkFragment.ChargeInterval.ANNUALLY.name,
            CreatePaymentLinkFragment.ChargeInterval.BI_ANNUAL.name.replace("_", " "),
            CreatePaymentLinkFragment.ChargeInterval.QUARTERLY.name,
            CreatePaymentLinkFragment.ChargeInterval.SEMI_ANNUAL.name.replace("_", " ")
        )
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        viewModel.createPlanObserver.observe(
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
                    refresh(it.data.data)
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Plan Successfully Created",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
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

    private fun listeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                dismiss()
                refresh(null)
            }
            createPlanBtn.setOnClickListener {
                val totalCount = if (binding.totalCountField.text.toString()
                    .isEmpty()
                ) 0 else binding.totalCountField.text.toString().toInt()
                validateFields {
                    val requestBody = APICreatePlanRequest(
                        planName = binding.planNameField.text.toString(),
                        planDescription = binding.planDescriptionField.text.toString(),
                        totalCount = totalCount,
                        planAmount = binding.amountField.text.toString(),
                        interval = interval.name
                    )
                    viewModel.setStateEvent(SubscriptionStateEvents.CreatePlan, requestBody)
                }
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
                        interval = chargeIntervals[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun validateFields(callback: () -> Unit) {
        if (binding.planNameField.text.toString().isEmpty()) {
            showError("Set a plan name")
            return
        }

        if (interval == CreatePaymentLinkFragment.ChargeInterval.NONE) {
            showError("Please set an Interval")
            return
        }
        if (binding.amountField.text.toString().isEmpty()) {
            showError("Set an amount")
            return
        }
        callback()
    }
}

@AndroidEntryPoint
class PlanFragment : Fragment(R.layout.plan_fragment_layout) {
    lateinit var binding: PlanFragmentLayoutBinding
    val viewModel: SubscriptionViwModel by viewModels()
    private val adapter: PlanFragmentAdapter by lazy {
        PlanFragmentAdapter {
            Navigation.findNavController(requireView()).navigate(
                SubscriptionFragmentDirections.actionSubscriptionFragmentToViewPlanFragment(
                    it
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PlanFragmentLayoutBinding.bind(view)
        viewModel.setStateEvent(SubscriptionStateEvents.GetPlans)
        initView()
    }

    private fun initView() {
        observer()
        setUpRecyclerView()
        listeners()
    }

    private fun listeners() {
        with(binding) {
            filterButton.setOnClickListener {
                FilterPlanDialog {
                }.show(requireActivity().supportFragmentManager, "FilterPlanDialog")
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observer() {
        with(viewModel) {
            plansObserver.observe(
                viewLifecycleOwner,
                {
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
                            adapter.plans = it.data.data.content.toMutableList()
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
            )
        }
    }

    fun loading(state: Boolean, progressBar: CircularProgressBar) {
        if (state) {
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
