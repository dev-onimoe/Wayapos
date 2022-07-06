package com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.payment.maincustomerdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentMainDetailsBinding
import com.wayapaychat.wayapay.databinding.PaymentFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.utils.ext.views.formatToNaira
import javax.inject.Inject

class MainDetailsFragment  @Inject constructor() : Fragment(R.layout.fragment_main_details) {

    lateinit var binding: FragmentMainDetailsBinding

//    val data = requireArguments().getParcelable<TransactionData>("key")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainDetailsBinding.bind(view)

         val data = requireArguments().getParcelable<TransactionData>("key")

        if (data != null) {

            binding.phoneTxtValue.text = data.customerPhone
            binding.customerIdValue.text = data.customerId
            binding.customerIdValue.text = data.customerId
            binding.SuccessfulPaymentsValue.text = "5"
            binding.TotalSpendValue.text = formatToNaira(data.amount)

        }

    }


}