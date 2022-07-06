package com.wayapaychat.wayapay.presentation.screens.home.wayapos_home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.WayaposHomeBinding
import com.wayapaychat.wayapay.presentation.screens.home.HomeFragmentDirections
import com.wayapaychat.wayapay.presentation.screens.refer_earn.ReferAndEarnActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WayaPosHomeFragment @Inject constructor() :
    Fragment(R.layout.wayapos_home) {

    lateinit var binding: WayaposHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WayaposHomeBinding.bind(view)
        initView()
    }

    private fun initView() {
       /* listeners()*/
    }

    /*private fun listeners() {
        with(binding) {
            posTransactionArea.setOnClickListener {
                navigateToTransactionScreen()
            }
            terminalsArea.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToTerminalsFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
            referAndEarn.setOnClickListener {
                startActivity(Intent(requireContext(), ReferAndEarnActivity::class.java))
            }
        }
    }*/

    private fun navigateToTransactionScreen() {
        val action = HomeFragmentDirections.actionHomeFragmentToTransactionFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}
