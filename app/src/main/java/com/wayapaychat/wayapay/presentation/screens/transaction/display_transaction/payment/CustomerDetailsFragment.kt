package com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.payment

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CustomerDetailsFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.CustomerTransactionFrag
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.ViewTransactionFragmentArgs
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.analytics.AnalyticsFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.payment.maincustomerdetails.MainDetailsFragment
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira
import com.wayapaychat.wayapay.presentation.utils.helper.FragmentSwapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.customer_details_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class CustomerDetailsFragment :
    BaseFragment<CustomerDetailsFragmentBinding>(R.layout.customer_details_fragment) {

    @Inject
    lateinit var detailsFragment: MainDetailsFragment

    private lateinit var transactionData: TransactionData


    lateinit var transactionFragment : CustomerTransactionFrag

    private var isPayment = true

    override fun init() {
        super.init()
        val bundle = Bundle()

        transactionData = ViewTransactionFragmentArgs
            .fromBundle(requireArguments())
            .data

        bundle.putParcelable("key", transactionData)
        detailsFragment.arguments = bundle
        initView()

        transactionFragment = CustomerTransactionFrag(transactionData.customerId)

    }

    private fun initView() {
        swapFragment(detailsFragment)
        populateView()
        listener()
    }

    private fun populateView() {
        binding.email.text = transactionData.customerEmail
        binding.statusChip.text = transactionData.customerName
    }

    private fun listener() {
        binding.detailsTxt.setOnClickListener {
            if (isPayment.not()) {
                swapFragment(detailsFragment)
                toggleTextColor()
                isPayment = true
            }
        }

        binding.transactionsTxt.setOnClickListener {
            if (isPayment) {
                swapFragment(transactionFragment)
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
            binding.detailsTxt.typeface = Typeface.DEFAULT
            binding.transactionsTxt.typeface = Typeface.DEFAULT_BOLD
        } else {
            binding.detailsTxt.typeface = Typeface.DEFAULT_BOLD
            binding.transactionsTxt.typeface = Typeface.DEFAULT
        }
    }

}
