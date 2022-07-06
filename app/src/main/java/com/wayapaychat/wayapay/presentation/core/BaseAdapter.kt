package com.wayapaychat.wayapay.presentation.core

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.wayapay.R

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder<ViewDataBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = getViewHolder(parent, viewType)

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding
    open fun getViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder(createBinding(parent, viewType))

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        (holder as BaseViewHolder<*>).binding.root.setTag(R.string.position, position)
        bind(holder.binding, position)
    }

    protected abstract fun bind(binding: ViewDataBinding, position: Int)
}
