package com.wayapaychat.wayapay.presentation.screens.auth.sign_up.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.BusinessTypeItemBinding
import com.wayapaychat.wayapay.databinding.BusinessTypesBottomSheetBinding
import com.wayapaychat.wayapay.framework.network.model.BusinessTypes
import com.wayapaychat.wayapay.framework.network.model.BusinessTypesItem
import com.wayapaychat.wayapay.presentation.core.BaseAdapter
import com.wayapaychat.wayapay.presentation.core.BaseBottomSheetDialog

class BusinessTypesBottomSheet(
    private val businessTypesItem: List<BusinessTypesItem>,
    private val callBack: (BusinessTypesItem) -> Unit
) : BaseBottomSheetDialog<BusinessTypesBottomSheetBinding>
(R.layout.business_types_bottom_sheet) {
    private val adapter: BusinessAdapter by lazy {
        BusinessAdapter {
            callBack(it)
            dismiss()
        }
    }

    override fun init() {
        super.init()
        setUpRecyclerView()
        adapter.businesstypes = businessTypesItem
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            this.adapter = this@BusinessTypesBottomSheet.adapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}

class BusinessAdapter(private val listener: (BusinessTypesItem) -> Unit) : BaseAdapter() {
    var businesstypes = listOf<BusinessTypesItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return BusinessTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val businessTypesItem = businesstypes[position]
        val businessBinding = binding as BusinessTypeItemBinding
        businessBinding.businessType.text = businessTypesItem.businessType
        binding.root.setOnClickListener {
            listener(businessTypesItem)
        }
    }

    override fun getItemCount() = businesstypes.size
}
