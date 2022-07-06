package com.wayapaychat.wayapay.presentation.screens.settings.kyc.bvn

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.BvnLinkedSuccessfullyBinding

class BVNLinkedSuccessFragment :
    Fragment(R.layout.bvn_linked_successfully) {
    lateinit var binding: BvnLinkedSuccessfullyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BvnLinkedSuccessfullyBinding.bind(view)
        init()
    }

    private fun init() {
        binding.doneBtn.setOnClickListener {
            it.findNavController()
                .navigate(BVNLinkedSuccessFragmentDirections.actionBVNLinkedSuccessFragmentToUpdateKycFragment2())
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireView())
                        .navigate(BVNLinkedSuccessFragmentDirections.actionBVNLinkedSuccessFragmentToUpdateKycFragment2())
                }
            }
        )
    }
}
