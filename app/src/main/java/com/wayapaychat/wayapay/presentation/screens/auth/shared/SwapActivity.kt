package com.wayapaychat.wayapay.presentation.screens.auth.shared

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.helper.FragmentSwapper
import com.wayapaychat.wayapay.presentation.utils.log.WayaPayLogger

abstract class SwapActivity<VB : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    BaseFullScreenActivity(), Navigation, FragmentTransaction {
    lateinit var binding: VB

    private var currentIndex = 0
    private var gotoLogin = false
    val fragments = mutableListOf<NavigationData>()

    override fun goBackTo() {
        TODO("Not yet implemented")
    }

    override fun goto() {
        TODO("Not yet implemented")
    }

    override fun navigate() {
        if (currentIndex < fragments.size - 1) {
            WayaPayLogger.d(TAG, "navigate: $currentIndex")
            currentIndex++
            WayaPayLogger.d(TAG, "navigate: $currentIndex ++")

            getCurrentActivity()
        }
        if (gotoLogin) {
            onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        init()
        loadFragment()
        displayFirstFragment()
    }

    private fun displayFirstFragment() {
        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            FragmentSwapper.addFragmentToActivity(
                supportFragmentManager,
                fragments[0].fragment,
                R.id.contentFrame
            )
        }
    }

    abstract fun init()

    override fun onBackPressed() {
        if (currentIndex > 0) {
            popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun getCurrentActivity() {
        FragmentSwapper.replaceFragmentInActivity(
            supportFragmentManager,
            fragments[currentIndex].fragment,
            R.id.contentFrame
        )
    }

    override fun popBackStack() {
        if (currentIndex > 0) {
            currentIndex--
            getCurrentActivity()
        } else {
            onBackPressed()
        }
    }

    private fun loadFragment() {
        if (fragments.isEmpty())
            WayaPayLogger.e(TAG, "loadFragment: please load fragments first")
        for (fragment in fragments) {
            fragment.fragment.setSignUpContract(this)
        }
    }

    fun addFragments(fragments: MutableList<NavigationData>) {
        this.fragments.addAll(fragments)
    }
}
