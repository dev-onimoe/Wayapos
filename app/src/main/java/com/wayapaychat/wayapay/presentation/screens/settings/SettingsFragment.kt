package com.wayapaychat.wayapay.presentation.screens.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SettingsLayoutBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.START_INDEX
import com.wayapaychat.wayapay.presentation.screens.settings.wayapay.WayaPaySettingFragment
import com.wayapaychat.wayapay.presentation.screens.settings.wayapos.WayaPosSettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsLayoutBinding>(R.layout.settings_layout) {

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listeners()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        with(binding) {
            val settingsViewPager = SettingsViewPager(requireActivity())
            viewPager.adapter = settingsViewPager
            viewPager.currentItem = START_INDEX
            viewPager.registerOnPageChangeCallback(object :
                    androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        toggle()
                    }
                })
        }
    }

    private fun toggle() {
    }

    private fun listeners() {}
}

class SettingsViewPager(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            WayaPaySettingFragment()
        } else {
            WayaPosSettingsFragment()
        }
    }
}
