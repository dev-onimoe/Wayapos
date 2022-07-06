package com.wayapaychat.wayapay.presentation.screens.home.subscription

import androidx.core.app.ShareCompat
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ViewSubscriptionLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.CustomerSubscriptionData
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_details.*

@AndroidEntryPoint
class ViewSubscriptionFragment :
    BaseFragment<ViewSubscriptionLayoutBinding>(R.layout.view_subscription_layout) {
    private lateinit var subscriptionData: CustomerSubscriptionData
    override fun init() {
        super.init()
        subscriptionData = ViewSubscriptionFragmentArgs
            .fromBundle(requireArguments())
            .subscriptionData
        initView()
    }
    private fun initView() {
        updateView()
    }

    private fun updateView() {
        with(binding) {
            planId.text = subscriptionData.planId
            statusValue.text = subscriptionData.status
            subCodeValue.text = subscriptionData.subscriptionId
            subDateValue.text = subscriptionData.startDate
            paymentValue.text = "₦0.00"
            intervalValue.text = "₦0.00"
            amountValue.text = "₦0.00"
            sublinkValue.text = subscriptionData.customerPaymentLink

            sublinkValue.setOnClickListener {
                val intent = ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setText(subscriptionData.customerPaymentLink)
                    .intent

                startActivity(intent)

            }
        }

    }
}
