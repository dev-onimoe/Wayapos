package com.wayapaychat.wayapay.presentation.screens.home.payment_link.payment_links.view_payment

import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.navigation.fragment.navArgs
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SubscriptionDetailsLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.Content
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionDetailsFragment :
    BaseFragment<SubscriptionDetailsLayoutBinding>(R.layout.subscription_details_layout) {

    private lateinit var content: Content
    override fun init() {
        super.init()
        val args: ViewSubPaymentLinkFragmentArgs by navArgs()
        content = args.content
        initView()
    }

    private fun initView() {
        populateView()
        listener()
    }

    private fun listener() {
        with(binding) {
            cancelButton.setOnClickListener {
            }
        }
    }

    private fun populateView() {
        with(binding) {
            // customerNameValue.text = content.cus
            planValue.text = content.paymentLinkName
            statusValue.text = content.status
            subsscriptionCodeValue.text = content.paymentLinkId
            amountValue.text = "NGN${content.amountText}"
            subscriptionDateValue.text = content.createdAt

            paymentValue.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(content.customerPaymentLink))
                startActivity(browserIntent)
            }
        }
    }
}
