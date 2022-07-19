package com.wayapaychat.wayapos.views.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapos.listeners.BackListener
import com.wayapaychat.wayapos.listeners.FragmentListener
import com.wayapaychat.wayapos.views.fragments.transaction.Agency_account_selection_fragment
import com.wayapaychat.wayapos.views.fragments.transaction.Terminal_transaction_fragment

class TransactionsActivity : AppCompatActivity(), FragmentListener {

    val terminal_layout : LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.terminal_layout)
    }

    val agency_layout : LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.agency_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        initializeViews()
    }

    private fun initializeViews() {
        terminal_layout.setOnClickListener{
            initFrag(Terminal_transaction_fragment(), supportFragmentManager)
        }
        agency_layout.setOnClickListener{
            initFrag(Agency_account_selection_fragment(), supportFragmentManager)
        }
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener{
            onBackPressed()
        }
    }

    private fun initFrag(frag : Fragment, fragManager : FragmentManager) {
        //val frag_manager = supportFragmentManager
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, frag).addToBackStack(null)
            .commit();
    }

    override fun switchFragments(fragment: Fragment, fragManager: FragmentManager?) {
        if (fragManager == null) {
            initFrag(fragment, supportFragmentManager)
        }else {
            fragManager!!.beginTransaction()
                .add(android.R.id.content, fragment).addToBackStack(null)
                .commit();
        }
    }

    fun goBack() {
        val currentFragment = getCurrentFragment()

        if (supportFragmentManager.getBackStackEntryCount() > 0) {

            removeCurrentFragment()
            supportFragmentManager.popBackStack()
        } else {
            onBackPressed()
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content)
    }

    private fun removeCurrentFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.remove(getCurrentFragment()!!)
        ft.commit()

        // not sure it is needed; will keep it as a reminder to myself if there will be problems
        // mFragmentManager.executePendingTransactions();
    }
}