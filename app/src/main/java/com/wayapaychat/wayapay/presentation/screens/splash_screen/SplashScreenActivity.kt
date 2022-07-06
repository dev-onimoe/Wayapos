package com.wayapaychat.wayapay.presentation.screens.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.MainActivity
import com.wayapaychat.wayapay.presentation.screens.onboarding.OnBoardingActivity
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.IS_LOGGED_IN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val SPLASH_SCREEN_TIME_OUT = 3000L
@AndroidEntryPoint
class SplashScreenActivity : BaseFullScreenActivity() {

    @Inject
    lateinit var cache: Cache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        initView()
    }

    private fun initView() {
        navigate()
    }

    private fun navigate() {

        Handler(Looper.myLooper()!!).postDelayed(
            {
                if (cache.getBoolean(IS_LOGGED_IN)) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, OnBoardingActivity::class.java)
                    startActivity(intent)
                }
                finish()
                overridePendingTransition(0, R.anim.fade_in_animation)
            },
            SPLASH_SCREEN_TIME_OUT
        )
    }
}
