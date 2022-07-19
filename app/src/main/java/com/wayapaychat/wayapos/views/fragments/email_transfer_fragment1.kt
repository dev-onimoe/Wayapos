package com.wayapaychat.wayapos.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.views.activities.TransferModeActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class email_transfer_fragment1 : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_email_transfer_fragment1, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view: View) {
        val recipient_type = view.findViewById<TextView>(R.id.tf_email_recipient_type)
        val spinner = view.findViewById<Spinner>(R.id.tf_email_recipient_type_spinner)
        val email = view.findViewById<EditText>(R.id.tf_email_mail)
        val fullName = view.findViewById<EditText>(R.id.tf_email_fullname)
        val proceed_btn = view.findViewById<Button>(R.id.email_tf_proceed1_btn)
        val error_text = view.findViewById<TextView>(R.id.email_tf_error_text1)

        proceed_btn.setOnClickListener {
            if (recipient_type.text.toString().equals("Select recipient type", true)) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please select recipient type"
            } else if (email.text.toString().equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please input recipient's email"
            } /*else if (fullName.text.toString().equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please select the account you are transferring from"
            }*/ else {

                val ownMap = HashMap<String, String>()
                ownMap.put("recipientMail", recipient_type.text.toString())
                ownMap.put("fullname", fullName.text.toString())

                val listener: FragmentListener = TransferModeActivity()
                listener.switchFragments(
                    email_transfer_fragment2(ownMap),
                    (activity as AppCompatActivity).supportFragmentManager
                )

            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            email_transfer_fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}