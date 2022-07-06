package com.wayapaychat.wayapay.presentation.screens.notification.wayapay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.wayapaychat.wayapay.databinding.NotificationItemBinding
import com.wayapaychat.wayapay.presentation.core.BaseAdapter

class NotificationAdapter : BaseAdapter() {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
    }

    override fun getItemCount() = 20
}
