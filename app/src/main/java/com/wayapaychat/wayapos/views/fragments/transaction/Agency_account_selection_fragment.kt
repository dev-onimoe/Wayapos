package com.wayapaychat.wayapos.views.fragments.transaction

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapos.models.*
import com.wayapaychat.wayapos.viewmodels.TerminalTransactionViewModel
import com.wayapaychat.wayapos.views.activities.TransactionsActivity
import com.wayapaychat.wayapos.views.adapters.WalletsAccountAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Agency_account_selection_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recycler : RecyclerView
    lateinit var no_transactions : TextView
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_agency_account_selection_fragment,
            container,
            false
        )
        initializeViews(view)
        initViewModels()
        return view
    }

    private fun initViewModels() {
        val viewModel = ViewModelProvider(activity as AppCompatActivity).get(
            TerminalTransactionViewModel::class.java)
        viewModel.context = activity as Context
        viewModel.cache = CacheImpl(activity as Context, Gson())
        viewModel.getwalletAccountsDataListObserver().observe(activity as AppCompatActivity, Observer<Response> {
            //println(it)
            when (it) {
                is Response.Success -> {
                    val walletAccountBody = ResponseBody(it.body)
                    val walletData= walletAccountBody.data
                    if (walletData is String) {
                        if (walletData.contains("No Found")) {
                            recycler.visibility = View.GONE
                            //no_transactions.text = transactionBody.respBody as String
                            no_transactions.visibility = View.VISIBLE
                        }else {
                            Toast.makeText(activity as Context, walletData, Toast.LENGTH_SHORT).show()
                        }

                    }else if (walletData is List<*>){

                        if (walletData.isEmpty()) {
                            recycler.visibility = View.GONE
                            //no_transactions.text = transactionBody.respBody as String
                            no_transactions.visibility = View.VISIBLE
                        }else {
                            val accounts = ArrayList<WalletAccount>()
                            for (item in walletData) {
                                accounts.add(WalletAccount(item as LinkedTreeMap<String, Any>))
                            }
                            progressBar.visibility = View.GONE
                            populateRecyclerView(accounts)
                        }

                    }
                }

                is Response.Failure -> {
                    Toast.makeText(
                        activity as Context,
                        it.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        viewModel.getWalletAccounts()
    }

    private fun initializeViews(view : View){

        val backIcon = view.findViewById<ImageView>(R.id.back_icon)
        progressBar = view.findViewById(R.id.progressBar)
        recycler = view.findViewById(R.id.accounts_recycler)
        no_transactions = view.findViewById(R.id.yhnwa_text)

        backIcon.setOnClickListener{
            (activity as TransactionsActivity).goBack()
        }
    }

    private fun populateRecyclerView(items : List<WalletAccount>) {
        val adapter = WalletsAccountAdapter(activity as Context, items)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Agency_account_selection_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}