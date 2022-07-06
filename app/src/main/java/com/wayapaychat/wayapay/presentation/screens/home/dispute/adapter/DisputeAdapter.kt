package com.wayapaychat.wayapay.presentation.screens.home.dispute.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.databinding.DisputeItemBinding
import com.wayapaychat.wayapay.databinding.SettlementItemBinding
import com.wayapaychat.wayapay.databinding.TransactionItemBinding
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.GetAllDisputeResponseDataContent
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira

class DisputeAdapter(private val listener: (data : GetAllDisputeResponseDataContent) -> Unit) : BaseAdapter() {
    var disputes = listOf<GetAllDisputeResponseDataContent>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DisputeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {

        /*val newformat: NumberFormat = NumberFormat.getCurrencyInstance()
        newformat.setMaximumFractionDigits(0)
        newformat.setCurrency(Currency.getInstance("NGN"))
*/
        val dispute = disputes[position]
        val businessBinding = binding as DisputeItemBinding
        businessBinding.titleTxt .text = "${dispute.customerFirstName} ${dispute.customerLastName}"
        businessBinding.descriptionTxt.text = dispute.customerEmail
        businessBinding.disputeDate.text = dispute.disputeInitiationDate.substring(0,10)

        binding.root.setOnClickListener {
            listener(disputes[position])
        }
    }

    override fun getItemCount() = disputes.size
}
