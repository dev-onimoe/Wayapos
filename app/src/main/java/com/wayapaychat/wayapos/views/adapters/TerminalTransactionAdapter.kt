package com.wayapaychat.wayapos.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.models.Transactions
import javax.inject.Inject

class TerminalTransactionAdapter(var context: Context, var transaction : List<Transactions>) :
    RecyclerView.Adapter<TerminalTransactionAdapter.TerminalTransactionViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TerminalTransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transactions_item, parent, false)
        return TerminalTransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TerminalTransactionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class TerminalTransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val amount  = itemView.findViewById<TextView>(R.id.transaction_amount)
        private val description  = itemView.findViewById<TextView>(R.id.transaction_description)
        private val time  = itemView.findViewById<TextView>(R.id.transaction_time)
    }
}