package com.wayapaychat.wayapos.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.models.WayaID
import com.wayapaychat.wayapos.views.activities.TransferModeActivity
import org.w3c.dom.Text


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WayaIdTransferFragment1 : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_waya_id_transfer1, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view : View) {

        val ids : List<WayaID> = ArrayList()

        val recyclerView = view.findViewById<RecyclerView>(R.id.id_recycler_view)
        val id_textview = view.findViewById<TextView>(R.id.tf_id_select_user)
        val amount = view.findViewById<TextView>(R.id.tf_id_amount)
        val description = view.findViewById<TextView>(R.id.tf_id_note)
        val spinner = view.findViewById<Spinner>(R.id.tf_id_spinner)
        val error = view.findViewById<TextView>(R.id.id_tf_error_text1)
        val proceed = view.findViewById<Button>(R.id.id_tf_proceed_btn)

        proceed.setOnClickListener{
            if (ids.size < 1) {
                error.text = "Please select a user from the dropdown"
                error.visibility = View.VISIBLE
            }else if(amount.text.toString().equals("")){
                error.text = "Please input amount"
                error.visibility = View.VISIBLE
            }else {

                val idslist = ArrayList<String>()

                for (id in ids) {
                    idslist.add(id.name)
                }

                val map = HashMap<String, Any>()
                map.put("amount", amount.text.toString())
                map.put("ids", idslist)
                if (!(description.text.toString().equals(""))) {
                    map.put("description", amount.text.toString())
                }
                val gson = Gson()
                val mapValues = gson.toJson(map)
                val pref = (activity as AppCompatActivity).getSharedPreferences("id_transfer", Context.MODE_PRIVATE)
                pref.edit().putString("id_transfer", mapValues).apply()
                val listener : FragmentListener = TransferModeActivity()
                listener.switchFragments(SummaryFragment(), (activity as AppCompatActivity).supportFragmentManager)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WayaIdTransferFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}