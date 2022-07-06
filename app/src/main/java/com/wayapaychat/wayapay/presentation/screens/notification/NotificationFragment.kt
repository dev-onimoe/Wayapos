package com.wayapaychat.wayapay.presentation.screens.notification

import androidx.fragment.app.viewModels
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.NotificationFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.notification.wayapay.WayapayNotificationFragment
import com.wayapaychat.wayapay.presentation.screens.notification.wayapay.WayapayNotificationViewModel
import com.wayapaychat.wayapay.presentation.screens.transaction.wayapay.WayaPayTransactionsViewModel
import com.wayapaychat.wayapay.presentation.utils.helper.FragmentSwapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment @Inject constructor() :
    BaseFragment<NotificationFragmentBinding>(R.layout.notification_fragment) {



    @Inject
    lateinit var wayapayNotificationFragment: WayapayNotificationFragment
    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        addFragment()
    }

    private fun addFragment() {
        FragmentSwapper.addFragmentToParentFragment(
            childFragmentManager.beginTransaction(),
            R.id.content,
            wayapayNotificationFragment
        )
    }
}
