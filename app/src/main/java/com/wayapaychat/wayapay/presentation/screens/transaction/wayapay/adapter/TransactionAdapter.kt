package com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.databinding.TransactionItemBinding
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira

class TransactionAdapter(private val listener: (data : TransactionData) -> Unit) : BaseAdapter() {
    var transactions = listOf<TransactionData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {

        /*val newformat: NumberFormat = NumberFormat.getCurrencyInstance()
        newformat.setMaximumFractionDigits(0)
        newformat.setCurrency(Currency.getInstance("NGN"))*/

        val transacction = transactions[position]
        val businessBinding = binding as TransactionItemBinding
        businessBinding.customer.text = transacction.customerName
        businessBinding.data = transacction
        val formated = formatToNaira(transacction.amount)
        businessBinding.amountTxt.setText(formated)
        businessBinding.textView3.text = transacction.tranDate?.substring(0,10)

        binding.root.setOnClickListener {
            listener(transactions[position])
        }
    }

    override fun getItemCount() = transactions.size
}
