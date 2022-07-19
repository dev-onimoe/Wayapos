package com.wayapaychat.wayapos.views.fragments.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapos.models.Response
import com.wayapaychat.wayapos.models.TerminalTransactionBody
import com.wayapaychat.wayapos.models.Transactions
import com.wayapaychat.wayapos.viewmodels.TerminalTransactionViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Terminal_transaction_fragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var cache : Cache
    lateinit var no_transactions : TextView
    lateinit var recycler : RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_terminal_transaction_fragment, container, false)
        initializeViews(view)
        initViewModels()
        return view
    }

    private fun initViewModels() {
        val viewModel = ViewModelProvider(activity as AppCompatActivity).get(TerminalTransactionViewModel::class.java)
        viewModel.context = activity as Context
        viewModel.cache = CacheImpl(activity as Context, Gson())
        viewModel.getDataListObserver().observe(activity as AppCompatActivity, Observer<Response> {
            //println(it)
            when (it) {
                is Response.Success -> {
                    val transactionBody = TerminalTransactionBody(it.body)
                    if (transactionBody.respBody is String) {
                        val resBody = transactionBody.respBody as String
                        if (resBody.contains("No Transaction Found")) {
                            recycler.visibility = View.GONE
                            //no_transactions.text = transactionBody.respBody as String
                            no_transactions.visibility = View.VISIBLE
                        }else {
                            Toast.makeText(
                                activity as Context,
                                resBody,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }else {
                        (transactionBody.respBody as? List<Transactions>).let {

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
        viewModel.getTerminalTransactions()
    }

    private fun initializeViews(view : View) {
        val filter_ic = view.findViewById<ImageView>(R.id.transaction_filter)
        //val back = view.findViewById<ImageView>(R.id.transaction_back)
        val terminal_number = view.findViewById<TextView>(R.id.merchant_terminal_id)
        val search_box = view.findViewById<EditText>(R.id.transaction_search_box)
        no_transactions = view.findViewById<TextView>(R.id.yhnty_text)
        recycler = view.findViewById<RecyclerView>(R.id.transactions_recycler_view)
        val spinner = view.findViewById<Spinner>(R.id.terminal_spinner)


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Terminal_transaction_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}