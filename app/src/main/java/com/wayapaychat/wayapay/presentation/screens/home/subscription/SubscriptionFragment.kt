package com.wayapaychat.wayapay.presentation.screens.home.subscription

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SubscriptionFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.START_INDEX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment :
    BaseFragment<SubscriptionFragmentBinding>(R.layout.subscription_fragment) {
    val viewModel: SubscriptionViwModel by viewModels()
    private var currentPosition = 0
    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        setUpViewPager()
        listeners()
    }

    private fun listeners() {

        with(binding) {
            planBtn.setOnClickListener {
                if (currentPosition == 1)
                    return@setOnClickListener
                viewPager.currentItem = 1
                toggleChip()
            }

            helpTxt.setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://wayapay.ng/developer"))
                startActivity(browserIntent)

            }

            subscriptionBtn.setOnClickListener {
                if (currentPosition == 0)
                    return@setOnClickListener
                viewPager.currentItem = 0
                toggleChip()
            }
            fab.setOnClickListener {
                if (currentPosition == 0) {
                    Navigation.findNavController(requireView())
                        .navigate(SubscriptionFragmentDirections.actionSubscriptionFragmentToCreateNewFragment())
                } else {
                    CreateNewPlanDialogFragment {}.show(
                        requireActivity().supportFragmentManager,
                        "CreateNewPlanDialogFragment"
                    )
                }
            }
        }
    }

    lateinit var subViewPager: SubViewPager
    private fun setUpViewPager() {
        with(binding) {
            subViewPager = SubViewPager(requireActivity())
            viewPager.adapter = subViewPager
            viewPager.currentItem = START_INDEX
            viewPager.registerOnPageChangeCallback(object :
                androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position
                    toggleChip()
                }
            })
        }
    }

    private fun toggleChip() {
        if (currentPosition == 0) {
            binding.subscriptionBtn.setBackgroundResource(R.drawable.chip_bgrd)
            binding.planBtn.setBackgroundResource(0)
            binding.subscriptionBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.deep_orange
                )
            )
            binding.planBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
        } else {
            binding.planBtn.setBackgroundResource(R.drawable.chip_bgrd)
            binding.subscriptionBtn.setBackgroundResource(0)
            binding.subscriptionBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            binding.planBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.deep_orange
                )
            )
        }
    }

    fun loading(state: Boolean) {
        loading(state, binding.progressBar)
    }
}
