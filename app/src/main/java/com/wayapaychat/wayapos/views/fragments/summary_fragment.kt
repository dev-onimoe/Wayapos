package com.wayapaychat.wayapos.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.chaos.view.PinView
import com.wayapaychat.wayapay.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SummaryFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.summary_fragment, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view : View) {

        val beneficiary = view.findViewById<TextView>(R.id.transfer_benefciary_text)
        val amount = view.findViewById<TextView>(R.id.summary_price)
        val destination_account = view.findViewById<TextView>(R.id.summary_to_account)
        val transaction_fee = view.findViewById<TextView>(R.id.summary_fee)
        val description = view.findViewById<TextView>(R.id.summary_description)
        val pinfield = view.findViewById<PinView>(R.id.summary_pinview)
        val confirm_btn = view.findViewById<Button>(R.id.summary_confirm_button)

        confirm_btn.setOnClickListener{

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SummaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}