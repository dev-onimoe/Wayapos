package com.wayapaychat.wayapay.presentation.screens.auth.sign_up

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wayapaychat.wayapay.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity :
    AppCompatActivity() {

//
//    @Inject
//    lateinit var signUpFragmentOne: SignUpFragmentOne
//    @Inject
//    lateinit var signUpFragmentTwo: SignUpFragmentTwo
//    @Inject
//    lateinit var signUpFragmentThree: SignUpFragmentThree
//    @Inject
//    lateinit var signUpOtpFragment: SignUpOtpFragment
//    override fun init() {
//        initView()
//    }

    private fun initView() {
        // loadFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)
    }
//
//    @Suppress("UNCHECKED_CAST")
//    private fun loadFragment() {
//        // stack all fragment
//        fragments.add(NavigationData(signUpFragmentOne as BaseNavigationFragment<ViewDataBinding>))
//        fragments.add(NavigationData(signUpFragmentTwo as BaseNavigationFragment<ViewDataBinding>))
//        fragments.add(NavigationData(signUpFragmentThree as BaseNavigationFragment<ViewDataBinding>))
//        fragments.add(NavigationData(signUpOtpFragment as BaseNavigationFragment<ViewDataBinding>))
//
//        for (fragment in fragments) {
//            fragment.fragment.setSignUpContract(this)
//        }
//    }
}
