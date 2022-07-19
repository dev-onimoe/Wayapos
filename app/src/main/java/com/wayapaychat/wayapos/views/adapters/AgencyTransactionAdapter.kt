package com.wayapaychat.wayapos.views.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.helperClasses.NumberFormats
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.models.AgencyTransactions
import com.wayapaychat.wayapos.models.WalletAccount
import com.wayapaychat.wayapos.views.activities.TransactionsActivity
import com.wayapaychat.wayapos.views.fragments.transaction.AgencyTransactionDetailsFragment
import com.wayapaychat.wayapos.views.fragments.transaction.Agency_account_transactions
import java.time.LocalDate

class AgencyTransactionAdapter(var context: Context, var transactions: List<AgencyTransactions>) :
    RecyclerView.Adapter<AgencyTransactionAdapter.AgencyTransactionViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AgencyTransactionViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.agency_transactions_item, parent, false)
        return AgencyTransactionViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AgencyTransactionViewHolder, position: Int) {
        val date = LocalDate.parse(transactions.get(position).tranDate)
        holder.date.text = date.toString()
        holder.type.text = transactions.get(position).tranType
        holder.ref.text = transactions.get(position).paymentReference

        if (transactions.get(position).partTranType == "C") {
            holder.amount.text = "+" + NumberFormats.formatToNGN(transactions.get(position).tranAmount!!.toDouble())
        }else {
            holder.amount.text = "-" + NumberFormats.formatToNGN(transactions.get(position).tranAmount!!.toDouble())
            holder.amount.setTextColor(context.resources.getColor(R.color.red))
            holder.cr_dr_image.setImageResource(R.drawable.debit_transaction_ic)
        }
        holder.itemView.setOnClickListener {
            val activity = context as TransactionsActivity
            activity.switchFragments(AgencyTransactionDetailsFragment(transactions.get(position)))

        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class AgencyTransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cr_dr_image = itemView.findViewById<ImageView>(R.id.transaction_type_image)
        val type = itemView.findViewById<TextView>(R.id.trans_description)
        val date = itemView.findViewById<TextView>(R.id.trans_date)
        val ref = itemView.findViewById<TextView>(R.id.trans_ref)
        val amount = itemView.findViewById<TextView>(R.id.trans_amount)

    }

}
