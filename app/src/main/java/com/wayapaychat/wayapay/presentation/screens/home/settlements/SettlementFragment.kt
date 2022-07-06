package com.wayapaychat.wayapay.presentation.screens.home.settlements

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SettlementFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.WayaPaySettlementsFragment
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.dialog.SettlementFilterDialog
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.dialog.TotalRevenueFilterDialog
import com.wayapaychat.wayapay.presentation.screens.transaction.dialog.FilterDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettlementFragment : BaseFragment<SettlementFragmentBinding>(R.layout.settlement_fragment){

    var status: String = ""
    var channel: String = ""
    var date: String = ""
    var search: String = ""

    @Inject
    lateinit var wayaPaySettlementFragment: WayaPaySettlementsFragment

    override fun init() {
        super.init()
        initView()

        val bundle = Bundle()
        bundle.putString("status", status)
        bundle.putString("channel", status)
        bundle.putString("date", date)
        bundle.putString("search", search)

        wayaPaySettlementFragment.arguments = bundle

    }

    private fun listeners() {
        with(binding) {
            filterButton.setOnClickListener {
                SettlementFilterDialog { status, channel, date ->
                    val bundle = Bundle()
                    bundle.putString("status", status)
                    bundle.putString("channel", channel)
                    bundle.putString("date", date)
                    bundle.putString("search", search)

                    wayaPaySettlementFragment.arguments = bundle
                    swapFragment(wayaPaySettlementFragment)
                }.show(requireActivity().supportFragmentManager, "Transaction_Fragment")
            }


            searchBar.addTextChangedListener(object : TextWatcher {
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

                    wayaPaySettlementFragment.arguments = bundle
                    swapFragment(wayaPaySettlementFragment)

                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

        }
    }

    private fun initView() {
        swapFragment(wayaPaySettlementFragment)
        listeners()
    }

    private fun swapFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().detach(fragment).replace(R.id.content, fragment)
            .commitNow()
        childFragmentManager.beginTransaction().attach(fragment).replace(R.id.content, fragment)
            .commitNow()
    }
}
