package com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CustomerDetailsLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.CustomerData2
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.CreateCustomerViewModel
import com.wayapaychat.wayapay.presentation.screens.home.customers.CustomersEvents
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint

const val NUMBER_OF_FRAGMENTS = 2
const val START_INDEX = 0

class ViewPager2(
    fragmentActivity: FragmentActivity,
    private val customerData: CustomerData2?
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = NUMBER_OF_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            CustomerDetailFrag(customerData)
        } else {
            CustomerTransactionFrag(customerData?.customerId)
        }
    }
}

@AndroidEntryPoint
class CustomerDetailsFragment :
    BaseFragment<CustomerDetailsLayoutBinding>(R.layout.customer_details_layout) {
    val viewModel: CreateCustomerViewModel by viewModels()
    private var customerData: CustomerData2? = null
    override fun init() {
        super.init()
        initView()
        val customerId = CustomerDetailsFragmentArgs.fromBundle(requireArguments()).customerId
        viewModel.setStateEvents(CustomersEvents.GetCustomer, customerId,"","",false)
    }

    private fun setUpViewPager() {
        with(binding) {
            viewPager.adapter = ViewPager2(requireActivity(), customerData)
            viewPager.currentItem = START_INDEX
            viewPager.registerOnPageChangeCallback(object :
                    androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        tablayout.selectTab(tablayout.getTabAt(position))
                    }
                })
        }
    }

    private fun initView() {
        observer()
        listener()
        setUpTabLayout()
    }

    private fun setUpTabLayout() {
        with(binding) {
            tablayout.addTab(
                tablayout.newTab().setText("Details")
            )
            tablayout.addTab(
                tablayout.newTab().setText("Transaction")

            )

            tablayout.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        viewPager.currentItem = tablayout.selectedTabPosition
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
        }
    }

    private fun listener() {
        with(binding) {
        }
    }

    private fun observer() {
        viewModel.customerObserver.observe(
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
                            positiveBottomText = "ok"
                        )
                        setUpViewPager()
                    }

                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        customerData = it.data.data
                        updateView(it.data.data)
                        setUpViewPager()
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Ok"
                        )
                        setUpViewPager()
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error?.message!!,
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                        setUpViewPager()
                    }
                }
            }
        )
    }

    private fun updateView(data: CustomerData2) {
        with(binding) {
            binding.nameFirstLetter.text = data.lastName[0].toString().toUpperCase()
            binding.emailTxt.text = data.email
            binding.nameTxt.text = "${data.lastName} ${data.firstName}"
        }
    }
}
