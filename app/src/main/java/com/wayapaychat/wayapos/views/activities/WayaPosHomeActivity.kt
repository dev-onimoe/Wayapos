package com.wayapaychat.wayapos.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapos.helperClasses.LocalCache
import com.wayapaychat.wayapos.models.AgencyTransactions
import com.wayapaychat.wayapos.models.Response
import com.wayapaychat.wayapos.models.ResponseBody
import com.wayapaychat.wayapos.models.ResponseBody2
import com.wayapaychat.wayapos.viewmodels.TerminalTransactionViewModel
import com.wayapaychat.wayapos.viewmodels.WayaposHomeViewModel
import kotlinx.android.synthetic.main.fragment_agency_account_transactions.*
import java.util.ArrayList

class WayaPosHomeActivity : AppCompatActivity() {

    val transferLayout : RelativeLayout by lazy {
        findViewById(R.id.transfer_layout)
    }
    val transactionsLayout : LinearLayout by lazy {
        findViewById(R.id.transaction_layout)
    }
    val org_name : TextView by lazy {

        findViewById(R.id.business_name_id)

    }
    val cache : Cache by lazy {
        CacheImpl(this, Gson())
    }

    val viewModel by lazy {
        ViewModelProvider(this).get(WayaposHomeViewModel::class.java)
    }
   lateinit var merchant : LinkedTreeMap<String, Any>
    lateinit var initial_selection_backview : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wayapos_landing)
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.context = this
        viewModel.cache = CacheImpl(this, Gson())
        getmerchants()
    }

    fun user(): APILoginResponse {
        val userDetails = cache.getObject(CacheConstants.Keys.USER_DETAILS, APILoginResponse::class.java) as APILoginResponse
        return userDetails
    }

    private fun getmerchants() {
        viewModel.getMerchantListObserver().observe(this, Observer<Response>{
            when (it) {
                is Response.Success -> {
                    val responseBody = ResponseBody2(it.body)
                    val merchants = responseBody.respBody as List<LinkedTreeMap<String, Any>>
                    for(items in merchants) {
                        if (user().data!!.user!!.phoneNumber == items.get("phoneNumber") ) {
                            merchant = items
                            cache.putObject("merchant", items)

                        }
                    }
                    if(merchant.get("orgName") != null) {
                        org_name.text = merchant.get("orgName") as String
                    }else {
                        org_name.text = "---"
                    }
                }

                is Response.Failure -> {

                    Toast.makeText(
                        this,
                        it.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        viewModel.merchantList()
    }


    private fun initViews() {
        transferLayout.setOnClickListener{
            //println("Transfer Clicked")

            showInitialTransferSelection()
        }
        transactionsLayout.setOnClickListener{
            //println("Transfer Clicked")
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }

        val total_rev_text = findViewById<TextView>(R.id.total_rev)
        val gross_rev = findViewById<TextView>(R.id.gross_revenue)
        val net_rev = findViewById<TextView>(R.id.net_revenue)
        val commission_tv = findViewById<TextView>(R.id.commission_revenue)
        //val org_name = findViewById<TextView>(R.id.business_name_id)
        val wallet_bal = findViewById<TextView>(R.id.walletBal)

        val cache = CacheImpl(this, Gson())

        total_rev_text.text = cache.getString("total")
        gross_rev.text = cache.getString("gross")
        net_rev.text = cache.getString("net")
        commission_tv.text = cache.getString("commission")
        //wallet_bal.text = cache.getString("total")

    }

    private fun showInitialTransferSelection() {
        val parentView = this.findViewById<FrameLayout>(R.id.landing_frame)
        val view1 = LayoutInflater.from(this).inflate(R.layout.transfer_initial_selection, parentView, false)
        val view = view1.findViewById<LinearLayout>(R.id.tf_funds_layout)
        val otherView = view1.findViewById<LinearLayout>(R.id.other_funds_transfer_layout)
        val own_transfer = view.findViewById<LinearLayout>(R.id.own_transfer_layout)
        val other_transfer = view.findViewById<LinearLayout>(R.id.other_transfer_layout)
        val pay_to_email = otherView.findViewById<LinearLayout>(R.id.pay_to_email_layout)
        val pay_to_wayaID = otherView.findViewById<LinearLayout>(R.id.pay_to_waya_layout)
        val scan_to_pay = otherView.findViewById<LinearLayout>(R.id.scan_to_pay_layout)
        val pay_to_phone = otherView.findViewById<LinearLayout>(R.id.pay_to_phone_layout)
        val pay_to_beneficiaries = otherView.findViewById<LinearLayout>(R.id.send_to_beneficiaries_layout)
        view.visibility = View.INVISIBLE
        val black_rl = view1.findViewById<RelativeLayout>(R.id.black_rl)
        parentView.addView(view1)
        //view.visibility = View.VISIBLE
        animateIn(view)
        black_rl.setOnClickListener{

            if (view.visibility == View.GONE) {
                hideInitialTransferSelection(otherView, 1000)
            }else if (otherView.visibility == View.GONE) {
                hideInitialTransferSelection(view, 1000)
            }
            removeSubView(view1, parentView)
        }

        own_transfer.setOnClickListener{
            callTransferActivity("own", black_rl)
        }

        other_transfer.setOnClickListener{
            //
            hideInitialTransferSelection(view, 1000)
            animateIn(otherView)
        }

        pay_to_email.setOnClickListener{
            callTransferActivity("email", black_rl)
        }

        pay_to_phone.setOnClickListener{
            callTransferActivity("phone", black_rl)
        }

        pay_to_wayaID.setOnClickListener{
            callTransferActivity("waya", black_rl)
        }

        scan_to_pay.setOnClickListener{
            callTransferActivity("scan", black_rl)
        }

        pay_to_beneficiaries.setOnClickListener{
            callTransferActivity("beneficiary", black_rl)
        }
    }

    private fun callTransferActivity(mode : String, view : View) {
        val intent = Intent(this, TransferModeActivity::class.java)
        intent.putExtra("mode", mode)
        startActivity(intent)
        view.performClick()
    }


    private fun animateIn(view : View) {
        hideInitialTransferSelection(view, 100)
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0.0F,  // toXDelta
            ((view.height).toFloat()) * 2,  // fromYDelta
            0F
        ) // toYDelta
        animate.duration = 1000
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    private fun hideInitialTransferSelection(view : View, time : Long) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            (view.height).toFloat()
        ) // toYDelta

        animate.duration = time
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.GONE

    }

    private fun removeSubView(view : View, parent : ViewGroup) {
        parent.removeView(view)
    }

}