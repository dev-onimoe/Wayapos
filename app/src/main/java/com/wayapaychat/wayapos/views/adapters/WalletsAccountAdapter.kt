package com.wayapaychat.wayapos.views.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.helperClasses.NumberFormats
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.models.Transactions
import com.wayapaychat.wayapos.models.WalletAccount
import com.wayapaychat.wayapos.views.activities.TransactionsActivity
import com.wayapaychat.wayapos.views.fragments.transaction.Agency_account_transactions

class WalletsAccountAdapter(var context: Context, var walletAccounts: List<WalletAccount>) :
    RecyclerView.Adapter<WalletsAccountAdapter.WalletsAccountViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletsAccountViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.agency_account_selection_item, parent, false)
        return WalletsAccountViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: WalletsAccountViewHolder, position: Int) {
        holder.accountnumber.text = walletAccounts.get(position).accountNo
        holder.balance.text = NumberFormats.formatToNaira(walletAccounts.get(position).clr_bal_amt)
        holder.itemView.setOnClickListener{
            val listener : FragmentListener = TransactionsActivity()
            listener.switchFragments(Agency_account_transactions(walletAccounts.get(position)), (context as AppCompatActivity).supportFragmentManager)
        }
    }

    override fun getItemCount(): Int {
        return walletAccounts.size
    }

    class WalletsAccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val accountnumber = itemView.findViewById<TextView>(R.id.walletAccount_actNo)
        val balance = itemView.findViewById<TextView>(R.id.walletAccount_price)

    }

}