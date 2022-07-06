package com.wayapaychat.wayapay.presentation.screens.home.subscription

import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ViewPlanFramgentBinding
import com.wayapaychat.wayapay.framework.network.model.PlanData
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPlanFragment : BaseFragment<ViewPlanFramgentBinding>(R.layout.view_plan_framgent) {

    private lateinit var planData: PlanData
    override fun init() {
        super.init()
        planData = ViewPlanFragmentArgs.fromBundle(requireArguments()).plan
        initView()
    }

    private fun initView() {
        populateViews()
        listeners()
    }

    private fun populateViews() {
        with(binding) {
            statusValue.text = planData.status
            planCodeValue.text = planData.planId
            planeNameValue.text = planData.planName
            planIntervalValue.text = planData.interval
            totalRevenueValue.text = "NGN" + planData.planAmount.toString()
            maxNumberPaymentValue.text = ""
            numberOfSubscriptionValue.text = ""
            dateCreatedTxtValue.text = ""
        }
    }

    private fun listeners() {
        with(binding) {
            deletePlanBtn.setOnClickListener {
            }
            updatePlanBtn.setOnClickListener {
            }
        }
    }
}
