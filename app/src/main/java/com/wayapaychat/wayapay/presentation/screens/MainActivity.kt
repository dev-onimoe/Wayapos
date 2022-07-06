package com.wayapaychat.wayapay.presentation.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ActivityMainBinding
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.AccountNumberData
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.screens.aboutus.AboutUsActivity
import com.wayapaychat.wayapay.presentation.screens.auth.login.LoginActivity
import com.wayapaychat.wayapay.presentation.screens.help_and_support.HelpAndSupportActivity
import com.wayapaychat.wayapay.presentation.screens.home.HomeFragment
import com.wayapaychat.wayapay.presentation.screens.home.HomeFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.privacy_policy.PrivacyPolicyActivity
import com.wayapaychat.wayapay.presentation.screens.refer_earn.ReferAndEarnActivity
import com.wayapaychat.wayapay.presentation.screens.security.SecurityActivity
import com.wayapaychat.wayapay.presentation.screens.settings.kyc.UpdateKycFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.settings.profile.ProfileActivity
import com.wayapaychat.wayapay.presentation.screens.settings.profile.ProfileActivity2
import com.wayapaychat.wayapay.presentation.screens.settings.profile.ProfileViewModel
import com.wayapaychat.wayapay.presentation.screens.terms_and_condition.TermsAndConditionActivity
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), HomeFragment.ManageDrawer {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    @Inject
    lateinit var cache: Cache
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        binding.navigationDrawer.bringToFront()
        initBottomNav()
        listeners()
        populateAccountDetails()
    }


    private fun listeners() {
        binding.wayabankTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, ComingSoonActivity::class.java))
        }
        binding.webTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, ComingSoonActivity::class.java))
        }
        binding.wayapayTab.setOnClickListener {
            toggleDrawer()
        }
        binding.aboutTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, AboutUsActivity::class.java))
        }
        binding.privacyTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, PrivacyPolicyActivity::class.java))
        }
        binding.termsTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, TermsAndConditionActivity::class.java))
        }
        binding.referTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, ReferAndEarnActivity::class.java))
        }
        binding.header.profileImage.setOnClickListener {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
        }
        binding.header.kycLevel.setOnClickListener {
            toggleDrawer()

            Navigation.findNavController(this.binding.navHostFragment)
                .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateKycFragment())

        }

        binding.securityTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, SecurityActivity::class.java))
        }
        binding.helpTab.setOnClickListener {
            toggleDrawer()
            startActivity(Intent(applicationContext, HelpAndSupportActivity::class.java))
        }
        binding.footer.setOnClickListener {
            toggleDrawer()
            cache.clear()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun populateAccountDetails() {
        val accountDetails = cache.getObject(
            CacheConstants.Keys.ACCOUNT_NUMBER,
            AccountNumberData::class.java
        ) as AccountNumberData

        val loginResponse =
            cache.getObject(
                CacheConstants.Keys.USER_DETAILS,
                APILoginResponse::class.java
            ) as APILoginResponse

        with(binding) {
            header.accountNumberValue.text = accountDetails.accountNumber
            header.accountName.text = accountDetails.accountName
            header.referValue.text = accountDetails.referalCode

            Glide.with(this@MainActivity).load(loginResponse.data?.user?.profileImage)
                .into(header.profileImage)

            header.copyIcon.setOnClickListener {
                val intent = ShareCompat.IntentBuilder.from(this@MainActivity)
                    .setType("text/plain")
                    .setText(getString(R.string.share_referal_code, accountDetails.referalCode))
                    .intent

                startActivity(intent)
            }
        }
    }

    override fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun initBottomNav() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)
    }
}
