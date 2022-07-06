package com.wayapaychat.wayapay.presentation.screens.refer_earn

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ReferAndEarnBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity

class ReferAndEarnActivity : BaseActivity() {
    lateinit var binding: ReferAndEarnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.refer_and_earn)
        initView()
    }

    private fun initView() {
        listener()
    }

    private fun listener() {
        with(binding) {
            invitedPeopleArea.setOnClickListener {
                startActivity(Intent(applicationContext, PeopleInvitedActivity::class.java))
            }
            moneyEarnedArea.setOnClickListener {
                startActivity(Intent(applicationContext, YourEarningsActivity::class.java))
            }
        }
    }
}
