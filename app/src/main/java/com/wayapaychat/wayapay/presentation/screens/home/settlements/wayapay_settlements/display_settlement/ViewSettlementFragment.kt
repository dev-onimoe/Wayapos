package com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.display_settlement

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentViewSettlementBinding
import com.wayapaychat.wayapay.databinding.ViewTransactionFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.ViewTransactionFragmentArgs
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira

class ViewSettlementFragment :
    BaseFragment<FragmentViewSettlementBinding>(R.layout.fragment_view_settlement) {

    private lateinit var settlementData: SettleContent

    override fun init() {
        super.init()

        settlementData = ViewSettlementFragmentArgs
            .fromBundle(requireArguments())
            .data

        populateView()
    }

    private fun populateView() {
        binding.amount.text = settlementData.settlementNetAmount.toString()
        binding.statusChip.text = settlementData.settlementStatus
        binding.dateTxt.text = settlementData.settlementDate.substring(0,9)
        binding.channelValue.text = settlementData.settlementBeneficiaryAccount
        binding.refrenceId.text = "Settlements > ${settlementData.settlementReferenceId}"
    }

}