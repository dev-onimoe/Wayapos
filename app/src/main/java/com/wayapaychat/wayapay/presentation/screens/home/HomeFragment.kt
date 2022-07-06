package com.wayapaychat.wayapay.presentation.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.HomeFragmentBinding
import com.wayapaychat.wayapay.databinding.WelcomeDialogBinding
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.help_and_support.HelpAndSupportActivity
import com.wayapaychat.wayapay.presentation.screens.home.customers.customer_details.START_INDEX
import com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements.dialog.TotalRevenueFilterDialog
import com.wayapaychat.wayapay.presentation.screens.home.wayapay_home.WayapayHomeFragment
import com.wayapaychat.wayapay.presentation.screens.home.wayapos_home.WayaPosHomeFragment
import com.wayapaychat.wayapay.presentation.screens.home.welcomescreen.WelcomeActivity
import com.wayapaychat.wayapay.presentation.screens.settings.profile.ProfileViewModel
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.analytics.AnalyticsFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionFragment
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionsViewModel
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.FIRST_LOGIN
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {
    private lateinit var manageDrawer: ManageDrawer
    private var currentPosition = 0
    val profileViewModel: ProfileViewModel by viewModels()
    val viewModel: WayaPayTransactionsViewModel by viewModels()

    interface ManageDrawer {
        fun toggleDrawer()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            manageDrawer = activity as ManageDrawer
        } catch (e: ClassNotFoundException) {

        }
    }

    @Inject
    lateinit var cache: Cache
    override fun init() {
        super.init()
        initView()
    }

    private fun showWelcomeDialog() {
        organisationName?.let {
            val dailog = WelcomeDialogFragment(it) {
                Navigation.findNavController(requireView())
                    .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateKycFragment())
            }
            dailog.show(requireActivity().supportFragmentManager, "WelcomeDialogFragment")
            dailog.isCancelable = false
            cache.putBoolean(FIRST_LOGIN, true).not()
        }
    }


    private fun filterRevenue(data: String) {

    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.rev_filter)

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.header1 -> {

                }
                R.id.header2 -> {
                    filterRevenue(item.title.toString())
                }
                R.id.header3 -> {
                    filterRevenue(item.title.toString())
                }
                R.id.header4 -> {
                    filterRevenue(item.title.toString())
                }
            }
            true
        }
        popup.show()
    }

    private fun initView() {
        listeners()
        observer()
        setUpViewPager(viewModel)
    }

    private fun setUpViewPager(viewModel: WayaPayTransactionsViewModel) {
        with(binding) {
            val settingsViewPager = HomeViewPager(requireActivity(),viewModel)
            viewPager.adapter = settingsViewPager
            viewPager.currentItem = START_INDEX
            viewPager.registerOnPageChangeCallback(object :
                androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position
                    toggleIndicator()
                }
            })
        }
    }

    private fun observer() {
        profileViewModel.profileObserver.observe(
            this
        ) {
            when (it) {
                is StateMachine.Loading -> {
                    // loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    // loading(false, binding.progressBar)
                    android.util.Log.d("TAG", "observer: ${it.toString()}")
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    // loading(false, binding.progressBar)
                   populateView(it.data.data)

                }

                is StateMachine.TimeOut -> {
                    // loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }
                is StateMachine.GenericError -> {
                    val message = it.error?.message ?: kotlin.run { "An error Occurred" }
                    showAlertDialogMessage(
                        message = message,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private var organisationName: String? = null
    private fun populateView(data: ProfileData?) {
        with(data) {
            val merchantId = cache.getString(
                CacheConstants.Keys.MERCHANT_ID
            )
           // organisationName = data?.otherDetails?.organisationName!!
            binding.businessName.text = "$merchantId"

            binding.businessName.setOnClickListener {
                val intent = ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setText(merchantId)
                    .intent

                startActivity(intent)
            }

        }
        if (cache.getBoolean(FIRST_LOGIN).not()) {

            showWelcomeDialog()
            cache.putBoolean(FIRST_LOGIN, true).not()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }
    }

    private fun listeners() {

        val merchantId = cache.getString(
            CacheConstants.Keys.MERCHANT_ID
        )

        with(binding) {
            dotsIc.setOnClickListener {
                manageDrawer.toggleDrawer()
            }
            productSwitcher.setOnClickListener {
                toggleIndicator()
            }

            notifications.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToNotificationFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
            calendar.setOnClickListener {
                //showPopup(calendar)

                TotalRevenueFilterDialog{ dateFrom, dateTo ->
                    viewModel.getTotalRevenue(dateTo,merchantId,dateFrom)

                }.show(requireActivity().supportFragmentManager, "Transaction_Fragment")

            }

            productSwitcher.setOnClickListener {
                if (currentPosition == 1) {
                    viewPager.currentItem = 0
                    productSwitcher.setImageResource(R.drawable.wayapay_active)
                } else {
                    viewPager.currentItem = 1
                    productSwitcher.setImageResource(R.drawable.wayapos_active)
                }
            }
        }
    }

    private fun toggleIndicator() {
        with(binding) {
            if (currentPosition == 1) {
                productSwitcher.setImageResource(R.drawable.wayapos_active)
            } else {
                productSwitcher.setImageResource(R.drawable.wayapay_active)
            }
        }
    }
}


class HomeViewPager(
    fragmentActivity: FragmentActivity, val viewModel: WayaPayTransactionsViewModel
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            WayapayHomeFragment(viewModel)
        } else {
            WayaPosHomeFragment()
        }
    }
}

class WelcomeDialogFragment(
    private val organisationName: String?,
    private val gotoKyc: () -> Unit
) : BaseDialogFragment(R.layout.welcome_dialog) {

    private lateinit var binding: WelcomeDialogBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WelcomeDialogBinding.bind(view)
        initView()
    }

    private fun initView() {
        binding.merchantTxt.text = organisationName
        listeners()
    }

    private fun listeners() {
        with(binding) {
            skipButton.setOnClickListener {
                dismiss()
            }
            completeKycBtn.setOnClickListener {
                gotoKyc()
                dismiss()
            }
        }
    }
}
