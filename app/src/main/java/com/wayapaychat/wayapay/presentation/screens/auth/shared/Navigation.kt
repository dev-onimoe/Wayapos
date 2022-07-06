package com.wayapaychat.wayapay.presentation.screens.auth.shared

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenFragment

interface Navigation {
    fun navigate()
    fun popBackStack()
}

abstract class BaseNavigationFragment<T : ViewDataBinding>(
    @LayoutRes layout: Int
) : BaseFullScreenFragment<T>(layout) {
    protected var navigation: Navigation? = null
    fun setSignUpContract(navigation: Navigation) {
        this.navigation = navigation
    }
}

interface FragmentTransaction {
    fun goto()
    fun goBackTo()
}

class NavigationData(val fragment: BaseNavigationFragment<ViewDataBinding>)
