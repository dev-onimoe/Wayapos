package com.wayapaychat.wayapay.presentation.screens.home.dispute.dispute_details

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.FragmentRejectChargeBackBinding
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.screens.home.dispute.WayaPayDisputeViewModel
import com.wayapaychat.wayapay.presentation.utils.PathUtil
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class RejectChargeBackFragment(val disputeID : String) : Fragment(R.layout.fragment_reject_charge_back) {

    lateinit var binding: FragmentRejectChargeBackBinding

    val viewModel: WayaPayDisputeViewModel by viewModels()

    private var imageFilePath: String? = ""
    private val REQUEST_CODE_IMAGE = 100

    private var selectedDocType = "Signed Invoices (electronic or paper)"


    var status = arrayOf(
        "Signed Invoices (electronic or paper)",
        "Signed Invoices (electronic or paper)",
        "Sales receipts (electronic or paper)",
        "Delivery confirmation documents",
        "Service usage times, dates, etc",
        "Proof that a digital product was downloaded by the customer")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRejectChargeBackBinding.bind(view)

        observer()

        binding.rejectChargeBackBtn.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            uploadImageDocument()
        }

        val spinnerAdapter = object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item, status) {

            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup?
            ): View? {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                    tv.setTextColor(Color.BLACK)

                return view
            }


        }


        binding.docType.apply {
            adapter = spinnerAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val ew  = if (status[position] == "select none" || status[position] == "") "" else status[position]
                    selectedDocType = ew
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }


        binding.imagefield.setOnClickListener {
            val chooseIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                val imageUri: Uri? = data?.clipData?.let {
                    it.getItemAt(0).uri
                } ?: data?.data

                if (imageUri == null) {
                    Log.e("TAG", "Invalid input image Uri.")
                    return
                }
                imageFilePath = PathUtil.getRealPath(requireContext(), imageUri)
                Log.d(TAG, "onActivityResult: $imageFilePath")

                /*Glide.with(requireContext()).load(imageUri).placeholder(R.drawable.place_holder)
                    .into(binding.profileImage)*/

                val filename: String? = imageFilePath?.substring(imageFilePath?.lastIndexOf("/")
                    ?.plus(1) ?: 1)

                binding.imagefield.text = (filename.toString())
            }
        }
    }


    private fun uploadImageDocument() {
        val file = File(imageFilePath)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val myfile = MultipartBody.Part.createFormData("files",file.name, requestBody)
        val merchantRejectionDocumentType = MultipartBody.Part.createFormData("merchantRejectionDocumentType", "Delivery confirmation documents")
        val disputeRejectReason = MultipartBody.Part.createFormData("disputeRejectReason", "Nothing")


        /*val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("merchantRejectionDocumentType", "Delivery confirmation documents")
            .addFormDataPart("disputeRejectReason", "nothing")
            .addFormDataPart("file", file.name,requestBody)
            .build()*/

        viewModel.rejectChargeBack(disputeID,merchantRejectionDocumentType,disputeRejectReason,myfile)
    }

    private fun observer() {
        viewModel.rejectChargeBackObserver.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is StateMachine.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }

                is StateMachine.Error -> {
                    binding.progress.visibility = View.GONE
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "ok"
                    )
                    Log.d("testing", it.error.toString())
                }

                is StateMachine.Success -> {
                    binding.progress.visibility = View.GONE

                    val dailog = SucccesAcceptChargeBackDialogueFragment(
                        message = "Your have successfully Rejected chargeback. this Dispute would be reviewed shortly.",
                        goHome = {
                            Navigation.findNavController(requireView()).popBackStack()
                        }
                    )

                    dailog.show(requireActivity().supportFragmentManager, "WelcomeDialogFragment")
                    dailog.isCancelable = false

                }

                is StateMachine.TimeOut -> {
                    binding.progress.visibility = View.GONE
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Ok"
                    )
                }
                is StateMachine.GenericError -> {
                    binding.progress.visibility = View.GONE
                    showAlertDialogMessage(
                        message = it.error?.data.toString(),
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }


}