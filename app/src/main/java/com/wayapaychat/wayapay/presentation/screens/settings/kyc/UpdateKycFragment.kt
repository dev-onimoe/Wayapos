package com.wayapaychat.wayapay.presentation.screens.settings.kyc

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.UpdateKycFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment

class UpdateKycFragment : BaseFragment<UpdateKycFragmentBinding>(R.layout.update_kyc_fragment) {

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun listeners() {
        with(binding) {
            tier1.setOnClickListener {
                Toast.makeText(requireContext(), "This Tier Level Is Complete", Toast.LENGTH_SHORT).show()
            }
            tier2.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(UpdateKycFragmentDirections.actionUpdateKycFragmentToAddBvnFragment())
            }
            tier3.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://app.staging.wayapay.ng/"))
                startActivity(browserIntent)
            }
            tier4.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://app.staging.wayapay.ng/"))
                startActivity(browserIntent)
            }
            tier5.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://app.staging.wayapay.ng/"))
                startActivity(browserIntent)
            }
        }
    }

    private fun observer() {
    }
}
