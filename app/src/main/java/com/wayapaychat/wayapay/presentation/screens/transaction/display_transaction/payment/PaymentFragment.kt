package com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.payment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.PaymentFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.presentation.screens.transaction.display_transaction.ViewTransactionFragmentDirections
import javax.inject.Inject

class PaymentFragment @Inject constructor() : Fragment(R.layout.payment_fragment) {

    lateinit var binding: PaymentFragmentBinding

    lateinit var data: TransactionData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PaymentFragmentBinding.bind(view)
        initView()

        data = requireArguments().getParcelable<TransactionData>("key")!!

        binding.customerName.text = data.customerName
        binding.dateTxt.text = data.rcre_time
        binding.referenceIdValue.text = data.refNo
        binding.channelValue.text = data.channel
        binding.wayapayFeeValue.text = data.fee.toString()
        binding.nameFirstLetter.text = data.customerName.substring(0,1)

    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            customerProfile.setOnClickListener {
                val action =
                    data.let { it1 ->
                        ViewTransactionFragmentDirections.actionViewTransactionFragmentToCustomerDetailsFragment(
                            it1
                        )
                    }
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }
}
