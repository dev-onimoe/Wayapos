package com.wayapaychat.wayapay.presentation.screens.home.payment_link.payment_links.view_payment

import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ViewSubLinkLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.Content
import com.wayapaychat.wayapay.presentation.core.BaseFragment

class ViewSubPaymentLinkFragment :
    BaseFragment<ViewSubLinkLayoutBinding>(R.layout.view_sub_link_layout) {
    private lateinit var content: Content
    override fun init() {
        super.init()
        initView()
        content = ViewSubPaymentLinkFragmentArgs.fromBundle(requireArguments()).content
    }

    private fun initView() {
        populateView()
    }

    private fun populateView() {
        with(binding) {

        }
    }
}
