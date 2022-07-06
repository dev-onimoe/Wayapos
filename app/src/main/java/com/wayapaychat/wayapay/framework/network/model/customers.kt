package com.wayapaychat.wayapay.framework.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants.TABLE.CUSTOMER

data class APICreateCustomerRequest(
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val email: String,
    @Expose
    val phoneNumber: String
)

data class APICreateCustomerResponse(
    @Expose
    val code: String,
    @Expose
    val message: String,
    @Expose
    val data: CustomerData,
    @Expose
    val date: String
)

data class CustomerData(
    @Expose
    val customerId: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val email: String,
    @Expose
    val phoneNumber: String,
    @Expose
    val createdAt: String,
    @Expose
    val createdBy: Int,
    @Expose
    val updatedAt: String,
    @Expose
    val merchantId: String,
    @Expose
    val status: String,
    @Expose
    val customerAvoided: String,

    @Expose
    val dateCustomerAvoided: String,
    @Expose
    val dateDeleted: String,
    @Expose
    val merchantKeyMode: String,
    @Expose
    val deleted: Boolean
)

data class APICustomersResponse(
    @Expose
    val code: String,
    @Expose
    val data: Data,
    @Expose
    val date: String,
    @Expose
    val message: String
)

@Entity(tableName = CUSTOMER)
data class CustomerContent(
    @Expose
    val createdAt: String,
    @Expose
    val createdBy: Int,
    @Expose
    val customerAvoided: Boolean?,
    @Expose
    @PrimaryKey(autoGenerate = false)
    val customerId: String,
    @Expose
    val dateCustomerAvoided: String?,
    @Expose
    val dateDeleted: String?,
    @Expose
    val deleted: Boolean,
    @Expose
    val email: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val merchantId: String,
    @Expose
    val merchantKeyMode: String,
    @Expose
    val phoneNumber: String,
    @Expose
    val status: String,
    @Expose
    val updatedAt: String,
    @Expose
    val updatedBy: String
)

data class Data(
    @Expose
    val content: List<CustomerContent>,
    @Expose
    val empty: Boolean,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val number: Int,
    @Expose
    val numberOfElements: Int,
    @Expose
    val pageable: CustomerPageable,
    @Expose
    val size: Int,
    @Expose
    val sort: CustomerSortX,
    @Expose
    val totalElements: Int,
    @Expose
    val totalPages: Int
)

data class CustomerPageable(
    @Expose
    val offset: Int,
    @Expose
    val pageNumber: Int,
    @Expose
    val pageSize: Int,
    @Expose
    val paged: Boolean,
    @Expose
    val sort: CustomerSort,
    @Expose
    val unpaged: Boolean
)

data class CustomerSort(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)

data class CustomerSortX(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)

data class APICustomerResponse(
    @Expose
    val code: String,
    @Expose
    val data: CustomerData2,
    @Expose
    val date: String,
    @Expose
    val message: String
)

data class CustomerData2(
    @Expose
    val customerId: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val email: String,
    @Expose
    val phoneNumber: String,
    @Expose
    val createdAt: String,
    @Expose
    val updatedAt: String,
    @Expose
    val createdBy: Int,
    @Expose
    val merchantId: String,
    @Expose
    val status: Any,
    @Expose
    val customerAvoided: Boolean,
    @Expose
    val dateCustomerAvoided: String,
    @Expose
    val dateDeleted: String,
    @Expose
    val merchantKeyMode: Any,
    @Expose
    val deleted: Boolean,
)
