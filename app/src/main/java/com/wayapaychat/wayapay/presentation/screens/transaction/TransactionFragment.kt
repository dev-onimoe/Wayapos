package com.wayapaychat.wayapay.presentation.screens.transaction

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.TransactionFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.dialog.CustomerFilterDialog
import com.wayapaychat.wayapay.presentation.screens.transaction.dialog.FilterDialog
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.analytics.AnalyticsFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment :
    BaseFragment<TransactionFragmentBinding>(R.layout.transaction_fragment) {

    @Inject
    lateinit var wayaPayTransactionFragment: WayaPayTransactionFragment

    var status: String = ""
    var channel: String = ""
    var date: String = ""
    var search: String = ""

    @Inject
    lateinit var analyticsFragment: AnalyticsFragment

    override fun init() {
        super.init()
        initView()

        val bundle = Bundle()
        bundle.putString("status", status)
        bundle.putString("channel", channel)
        bundle.putString("date", date)
        bundle.putString("search", search)


        wayaPayTransactionFragment.arguments = bundle

    }

    private fun initView() {
        swapFragment(wayaPayTransactionFragment)
        listeners()
    }

    private fun listeners() {
        with(binding) {
            filterButton.setOnClickListener {
                FilterDialog { status, channel, date ->

                    val bundle = Bundle()
                    bundle.putString("status", status)
                    bundle.putString("channel", channel)
                    bundle.putString("date", date)
                    bundle.putString("search", search)

                    wayaPayTransactionFragment.arguments = bundle
                    swapFragment(wayaPayTransactionFragment)
                }.show(requireActivity().supportFragmentManager, "Transaction_Fragment")
            }

            searchBar.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    search = s.toString()

                    val bundle = Bundle()
                    bundle.putString("status", status)
                    bundle.putString("channel", channel)
                    bundle.putString("date", date)
                    bundle.putString("search", search)

                    wayaPayTransactionFragment.arguments = bundle
                    swapFragment(wayaPayTransactionFragment)

                }

                override fun afterTextChanged(s: Editable?) {

                }
            })


        }

    }

    private fun swapFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().detach(fragment).replace(R.id.content, fragment)
            .commitNow()
        childFragmentManager.beginTransaction().attach(fragment).replace(R.id.content, fragment)
            .commitNow()
    }
}
