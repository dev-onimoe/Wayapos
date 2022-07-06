package com.wayapaychat.wayapay.presentation.screens.home.dispute.dispute_details


import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentDisputeDetailsBinding
import com.wayapaychat.wayapay.framework.network.model.CustomerData2
import com.wayapaychat.wayapay.framework.network.model.GetAllDisputeResponseDataContent
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.CustomerDetailFrag
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.CustomerTransactionFrag
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.ViewPager2
import dagger.hilt.android.AndroidEntryPoint


const val NUMBER_OF_FRAGMENTS = 2
const val START_INDEX = 0

class ViewPager3(
    fragmentActivity: FragmentActivity,
    val amount : Double,
    val disputeId : String
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = NUMBER_OF_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            AcceptChargeBackFragment(amount = amount,disputeId)
        } else {
            RejectChargeBackFragment(disputeId)
        }
    }
}

@AndroidEntryPoint
class DisputeDetailsFragment : BaseFragment<FragmentDisputeDetailsBinding>(R.layout.fragment_dispute_details){

    private lateinit var data: GetAllDisputeResponseDataContent

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {

        data = DisputeDetailsFragmentArgs
            .fromBundle(requireArguments()).data

        val disputeStatus = when (data.disputeResolutionStatus){
            "CUSTOMER_WON" -> { "LOST"}
            "MERCHANT_WON" -> { "WON"}
            else -> {data.disputeResolutionStatus}
        }

        when (disputeStatus) {
            "LOST" -> {
                binding.statusValue.setTextColor(Color.parseColor("#F92E2E"))
            }
            "WON" -> {
                binding.statusValue.setTextColor(Color.parseColor("#27AE60"))
            }
            else -> {

            }
        }

        binding.customerEmailValue.text = data.customerEmail
        binding.transactionValue.text = "₦ ${data.transactionAmount}"
        binding.customerNameValue.text = "${data.customerName} ${data.customerLastName}"
        binding.DisputeTypeValue.text = data.disputeType
        binding.dueInValue.text = data.merchantResponseDueDate
        binding.ReferenceIDValue.text = data.referenceId
        binding.reasonVal.text = data.reasonForDisputeInDetails
        binding.statusValue.text = disputeStatus
        binding.CreatedAtValue.text = data.dateCreated.substring(0,10)


        setUpTabLayout()
        setUpViewPager()
    }

    private fun setUpTabLayout() {
        with(binding) {
            tablayout.addTab(
                tablayout.newTab().setText("Accept Chargeback")
            )

            tablayout.addTab(
                tablayout.newTab().setText("Reject Chargeback")

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

    private fun setUpViewPager() {
        with(binding) {
            viewPager.adapter = ViewPager3(requireActivity(),data.transactionAmount,data.merchantCustomerDisputeId)
            viewPager.currentItem = START_INDEX
            viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tablayout.selectTab(tablayout.getTabAt(position))
                }
            })
        }
    }

}