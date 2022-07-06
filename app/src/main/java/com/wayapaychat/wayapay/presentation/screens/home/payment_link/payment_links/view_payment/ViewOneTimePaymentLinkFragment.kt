package com.wayapaychat.wayapay.presentation.screens.home.payment_link.payment_links.view_payment

import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ViewOneTimePaymentBinding
import com.wayapaychat.wayapay.framework.network.model.Content
import com.wayapaychat.wayapay.presentation.core.BaseFragment


class ViewOneTimePaymentLinkFragment :
    BaseFragment<ViewOneTimePaymentBinding>(R.layout.view_one_time_payment) {
    lateinit var content: Content
    override fun init() {
        super.init()
        content = ViewOneTimePaymentLinkFragmentArgs.fromBundle(requireArguments()).content
        initView()
    }

    private fun initView() {
        populateView()
    }

    private fun populateView() {
        with(binding) {
            amountTxt.text = content.amountText
            paymentId.text = content.paymentLinkId
            statusValue.text = content.status
            pageNameValue.text = content.paymentLinkName
            paymentTypeValue.text = content.paymentLinkType
            paymentTypeValue.text = content.paymentLinkType
            paymentTypeUrlValue.text = content.customerPaymentLink
            dateCreatedValue.text = content.createdAt

            paymentTypeUrlValue.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(content.customerPaymentLink))
                startActivity(browserIntent)
            }
        }
    }
}
