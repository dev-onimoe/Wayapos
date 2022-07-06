package com.wayapaychat.wayapay.presentation.screens.home.dispute.dispute_details

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentResolvedDisputeBinding
import com.wayapaychat.wayapay.framework.network.model.GetAllDisputeResponseDataContent
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.display_settlement.ViewSettlementFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResolvedDisputeFragment :
    BaseFragment<FragmentResolvedDisputeBinding>(R.layout.fragment_resolved_dispute) {

    private lateinit var data: GetAllDisputeResponseDataContent

    override fun init() {
        super.init()
        data = ResolvedDisputeFragmentArgs
            .fromBundle(requireArguments()).data

        val disputeStatus = when (data.disputeResolutionStatus) {
            "CUSTOMER_WON" -> {
                "LOST"
            }
            "MERCHANT_WON" -> {
                "WON"
            }
            else -> {
                data.disputeResolutionStatus
            }
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
        binding.transactionValue.text = "₦ ${data.transactionAmount.toString()}"
        binding.customerNameValue.text = "${data.customerName} ${data.customerLastName}"
        binding.DisputeTypeValue.text = data.disputeType
        binding.dueInValue.text = data.merchantResponseDueDate
        binding.ReferenceIDValue.text = data.referenceId
        binding.reasonVal.text = data.reasonForDisputeInDetails
        binding.statusValue.text = disputeStatus
        binding.CreatedAtValue.text = data.dateCreated.substring(0, 10)


    }

}