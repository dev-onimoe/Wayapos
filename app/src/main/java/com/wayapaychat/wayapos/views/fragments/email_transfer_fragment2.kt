package com.wayapaychat.wayapos.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.views.activities.TransferModeActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class email_transfer_fragment2(map : HashMap<String, String>) : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var map = map

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
        val view = inflater.inflate(R.layout.fragment_email_transfer_fragment2, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view : View) {

        val amount = view.findViewById<TextView>(R.id.tf_email_amount)
        val description = view.findViewById<TextView>(R.id.tf_email_note)
        val proceed_btn = view.findViewById<Button>(R.id.email_tf_proceed2_btn)
        val error_text = view.findViewById<TextView>(R.id.email_tf_error_text2)

        proceed_btn.setOnClickListener{

            if (amount.text.toString().equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please input amount"
            }/*else if (description.text.toString().equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please select the account you are transferring from"
            }*/else {
                map.put("amount", amount.text.toString())
                if (!(description.text.toString().equals(""))) {
                    map.put("description", amount.text.toString())
                }
                val gson = Gson()
                val mapValues = gson.toJson(map)
                val pref = (activity as AppCompatActivity).getSharedPreferences("email_transfer", Context.MODE_PRIVATE)
                pref.edit().putString("email_transfer", mapValues).apply()
                val listener : FragmentListener = TransferModeActivity()
                listener.switchFragments(SummaryFragment(), (activity as AppCompatActivity).supportFragmentManager)

            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, map : HashMap<String, String>) =
            email_transfer_fragment2(map).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}