package com.wayapaychat.wayapos.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.views.fragments.OwnTFFragment1

class TransferModeActivity : AppCompatActivity(), FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_mode)

        val mode = intent.getStringExtra("mode")
        if (mode.equals("own")) {
            val frag = OwnTFFragment1()
            initFrag(frag, supportFragmentManager)
        }else {
            when (mode) {
                "email" -> {
                    val frag = OwnTFFragment1(); initFrag(frag, supportFragmentManager)
                }
                "phone" -> {
                    val frag = OwnTFFragment1(); initFrag(frag, supportFragmentManager)
                }
                "waya" -> {
                    val frag = OwnTFFragment1(); initFrag(frag, supportFragmentManager)
                }
                "beneficiary" -> {
                    val frag = OwnTFFragment1(); initFrag(frag, supportFragmentManager)
                }
                "scan" -> {
                    val frag = OwnTFFragment1(); initFrag(frag, supportFragmentManager)
                }
            }
        }
    }

    override fun switchFragments(fragment: Fragment, fragManager: FragmentManager?) {
        if (fragManager == null) {
            initFrag(fragment, supportFragmentManager)
        }else {
            initFrag(fragment, fragManager!!)
        }
    }

    private fun initFrag(frag : Fragment, fragManager : FragmentManager) {

        //val frag_manager = supportFragmentManager
        val transaction = fragManager.beginTransaction()
        transaction.replace(R.id.tf_fragment_container, frag)
        transaction.commit()
    }
}