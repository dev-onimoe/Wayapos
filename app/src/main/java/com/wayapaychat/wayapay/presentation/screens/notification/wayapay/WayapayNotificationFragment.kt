package com.wayapaychat.wayapay.presentation.screens.notification.wayapay

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.WayapayNotificationFragBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.notification.wayapay.adapter.NotificationAdapter
import javax.inject.Inject

class WayapayNotificationFragment @Inject constructor() : BaseFragment<WayapayNotificationFragBinding> (R.layout.wayapay_notification_frag) {

    val viewModel: WayapayNotificationViewModel by viewModels()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter()
    }

    override fun init() {
        super.init()
        initView()
    }
    private fun initView() {
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        binding.recyclerView.apply {
            adapter = this@WayapayNotificationFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}
