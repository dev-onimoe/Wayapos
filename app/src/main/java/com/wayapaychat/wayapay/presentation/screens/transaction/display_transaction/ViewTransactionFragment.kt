package com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ViewTransactionFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.analytics.AnalyticsFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.payment.PaymentFragment
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira
import com.wayapaychat.wayapay.presentation.utils.helper.FragmentSwapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewTransactionFragment :
    BaseFragment<ViewTransactionFragmentBinding>(R.layout.view_transaction_fragment) {
    @Inject
    lateinit var paymentFragment: PaymentFragment

    private lateinit var transactionData: TransactionData

    @Inject
    lateinit var analyticsFragment: AnalyticsFragment

    private var isPayment = true

    override fun init() {
        super.init()
        val bundle = Bundle()

        transactionData = ViewTransactionFragmentArgs
            .fromBundle(requireArguments())
            .data

        bundle.putParcelable ("key", transactionData)
        paymentFragment.arguments = bundle
        initView()
    }

    private fun initView() {
        swapFragment(paymentFragment)
        populateView()
        listener()
        binding.data = transactionData
    }

    private fun populateView() {

        binding.amount.text = formatToNaira(transactionData.amount)
        binding.statusChip.text = transactionData.status
    }

    private fun listener() {
        binding.paymentText.setOnClickListener {
            if (isPayment.not()) {
                swapFragment(paymentFragment)
                toggleTextColor()
                isPayment = true
            }
        }

        binding.analyticsTxt.setOnClickListener {
            if (isPayment) {
                swapFragment(analyticsFragment)
                toggleTextColor()
                isPayment = false
            }
        }
    }

    private fun swapFragment(fragment: Fragment) {
        FragmentSwapper.addFragmentToParentFragment(
            childFragmentManager.beginTransaction(),
            R.id.content,
            fragment
        )
    }

    private fun toggleTextColor() {
        if (isPayment) {
            binding.paymentText.typeface = Typeface.DEFAULT
            binding.analyticsTxt.typeface = Typeface.DEFAULT_BOLD
        } else {
            binding.paymentText.typeface = Typeface.DEFAULT_BOLD
            binding.analyticsTxt.typeface = Typeface.DEFAULT
        }
    }
}