package com.wayapaychat.wayapos.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.wayapaychat.wayapay.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Transfer_Complete_Fragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_transfer_complete, container, false)
        initializeViews(view)
        return view
    }

    private fun initializeViews(view : View) {

        val okay_btn = view.findViewById<Button>(R.id.summary_confirm_button)

        okay_btn.setOnClickListener{
            activity?.let {
                it.finish()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Transfer_Complete_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}