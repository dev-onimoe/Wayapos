package com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.databinding.SettlementItemBinding
import com.wayapaychat.wayapay.databinding.TransactionItemBinding
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira

class SettlementAdapter(private val listener: (data : SettleContent) -> Unit) : BaseAdapter() {
    var settlements = listOf<SettleContent>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return SettlementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {

        /*val newformat: NumberFormat = NumberFormat.getCurrencyInstance()
        newformat.setMaximumFractionDigits(0)
        newformat.setCurrency(Currency.getInstance("NGN"))
*/
        val settlement = settlements[position]
        val businessBinding = binding as SettlementItemBinding
        businessBinding.customer.text = settlement.settlementReferenceId
        businessBinding.data = settlement
        val formated = formatToNaira(settlement.settlementNetAmount)
        businessBinding.amountTxt.setText(formated)
        businessBinding.date.text = settlement.settlementDate.substring(0,10)

        binding.root.setOnClickListener {
            listener(settlements[position])
        }
    }

    override fun getItemCount() = settlements.size
}
