package com.wayapaychat.wayapay.presentation.screens.onboarding

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.OnboardingLayoutBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.login.LoginActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.one.SignUpFragmentOne
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.getImageDrawable
import com.wayapaychat.wayapay.presentation.utils.ext.views.removeOverScroll
import com.wayapaychat.wayapay.presentation.utils.log.WayaPayLogger

const val SWIPE_TIME = 2000L

data class InfoData(
    @DrawableRes val image: Drawable,
    val title: String,
    val description: String,
    @DrawableRes val firstIndicator: Drawable,
    @DrawableRes val secondIndicator: Drawable,
    @DrawableRes val thirdIndicator: Drawable
)

class OnBoardingActivity : BaseActivity() {
    lateinit var binding: OnboardingLayoutBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentSliderPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.onboarding_layout)
        initView()
    }

    private val infoAdapter by lazy {
        InfoAdapter()
    }

    private var infoData = ArrayList<InfoData>()
    private fun setUpViewPager() {
        binding.infoViewPager.adapter = infoAdapter
        infoAdapter.infoData = infoData
        binding.infoViewPager.clipToPadding = false
        binding.infoViewPager.clipChildren = false
        binding.infoViewPager.offscreenPageLimit = 3
        binding.infoViewPager.removeOverScroll()
        binding.infoViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentSliderPosition = position + 1
                    updateIndicatorAndMessages(position)
                    updateVisibility()
                }
            })
    }

    private fun updateIndicatorAndMessages(position: Int) {
        with(infoData[position]) {
            Glide.with(applicationContext)
                .load(firstIndicator)
                .into(binding.indicatorOne)
            Glide.with(applicationContext)
                .load(secondIndicator)
                .into(binding.indicatorTwo)
            Glide.with(applicationContext)
                .load(thirdIndicator)
                .into(binding.indicatorThree)
            binding.titletxt.text = title
            binding.descriptiontxt.text = description
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initView() {
        setUpInfoData()
        setUpViewPager()
        listener()
    }

    private fun listener() {
        with(binding) {

            getStartedBtn.setOnClickListener {
                if (currentSliderPosition < infoData.size) {
                    WayaPayLogger.d(
                        TAG,
                        "info size: " + (infoData.size - 1) + " slider ps: " + currentSliderPosition
                    )
                    infoViewPager.currentItem = infoViewPager.currentItem + 1
                    updateVisibility()
                } else {
                    startActivity(Intent(applicationContext, SignUpFragmentOne::class.java))
                }
            }
            skipTxt.setOnClickListener {
                skip()
            }

            loginBtn.setOnClickListener {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }

    private fun updateVisibility() {
        with(binding) {

            infoViewPager.let {
                if (it.currentItem == 2) {
                    skipTxt.visibility = View.INVISIBLE
                    getStartedBtn.text = getString(R.string.get_started)
                    loginBtn.visibility = View.VISIBLE
                } else {
                    skipTxt.visibility = View.VISIBLE
                    getStartedBtn.text = getString(R.string.next)
                    loginBtn.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun skip() {
        binding.infoViewPager.currentItem = 2
        updateVisibility()
    }

    private fun setUpInfoData() {
        infoData.add(
            InfoData(
                image = getImageDrawable(R.drawable.onboarding_img__3),
                title = getString(R.string.onboarding_title_one),
                description = getString(R.string.onboarding_description_txt_one),
                firstIndicator = getImageDrawable(R.drawable.indicator_active_ic),
                secondIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
                thirdIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
            )
        )
        infoData.add(
            InfoData(
                image = getImageDrawable(R.drawable.onboarding_img_2),
                title = getString(R.string.onboarding_title_two),
                description = getString(R.string.onboarding_description_txt_two),
                firstIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
                secondIndicator = getImageDrawable(R.drawable.indicator_active_ic),
                thirdIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
            )
        )
        infoData.add(
            InfoData(
                image = getImageDrawable(R.drawable.onboarding_img_1),
                title = getString(R.string.onboarding_title_three),
                description = getString(R.string.onboarding_description_txt_three),
                firstIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
                secondIndicator = getImageDrawable(R.drawable.indicator_inactive_ic),
                thirdIndicator = getImageDrawable(R.drawable.indicator_active_ic),
            )
        )
    }
}
