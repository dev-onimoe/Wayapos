package com.wayapaychat.wayapay.presentation.screens.home.payment_link

import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.PaymentLinkBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import kotlinx.android.synthetic.main.create_payment_link.*

class PaymentLinkFragment : BaseFragment<PaymentLinkBinding>(R.layout.payment_link) {

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            createLinkBtn.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(PaymentLinkFragmentDirections.actionPaymentLinkFragmentToPaymentLinksFragment())
            }
        }
    }
}
