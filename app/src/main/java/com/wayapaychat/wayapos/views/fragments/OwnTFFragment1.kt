package com.wayapaychat.wayapos.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.views.activities.TransferModeActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OwnTFFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_own_tf1, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view : View) {

        val from_textview = view.findViewById<TextView>(R.id.tf_from)
        val to_textview = view.findViewById<TextView>(R.id.tf_to)
        val fromSpinner = view.findViewById<Spinner>(R.id.from_spinner)
        val toSpinner = view.findViewById<Spinner>(R.id.to_spinner)
        val amt_editText = view.findViewById<EditText>(R.id.tf_own_amount)
        val note_editText = view.findViewById<EditText>(R.id.tf_own_note)
        val error_text = view.findViewById<TextView>(R.id.own_tf_error_text)
        val sendBtn = view.findViewById<Button>(R.id.own_tf_send_btn)

        var from_text = ""
        var to_text = ""

        val ad: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            activity as Context,
            R.array.recipient_type,
            android.R.layout.simple_spinner_item
        )
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = ad
        toSpinner.adapter = ad

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                from_text = resources.getStringArray(R.array.recipient_type)[p2]
                from_textview.text = from_text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                to_text = resources.getStringArray(R.array.recipient_type)[p2]
                to_textview.text = to_text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        from_textview.setOnClickListener{
            fromSpinner.performClick()
        }
        to_textview.setOnClickListener{
            toSpinner.performClick()
        }
        sendBtn.setOnClickListener{
            if (from_textview.text.equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please select the account you are transferring from"
            }else if(to_textview.text.equals("")) {
                error_text.visibility = View.VISIBLE
                error_text.text = "Please input the account you are transferring to"
            }else if (amt_editText.text.toString().equals("")){
                error_text.visibility = View.VISIBLE
                error_text.text = "Please input an amount"
            }else {
                val ownMap = HashMap<String, String>()
                ownMap.put("from", from_text)
                ownMap.put("to", to_text)
                ownMap.put("amount", amt_editText.text.toString())
                if (!(note_editText.text.toString().equals(""))) {
                    ownMap.put("description", amt_editText.text.toString())
                }
                val gson = Gson()
                val mapValues = gson.toJson(ownMap)
                val pref = (activity as AppCompatActivity).getSharedPreferences("own_transfer", Context.MODE_PRIVATE)
                pref.edit().putString("own_transfer", mapValues).apply()
                val listener : FragmentListener = TransferModeActivity()
                listener.switchFragments(SummaryFragment(), (activity as AppCompatActivity).supportFragmentManager)
            }
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            OwnTFFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}