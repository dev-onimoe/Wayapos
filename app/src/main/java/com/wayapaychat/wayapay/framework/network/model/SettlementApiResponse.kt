package com.wayapaychat.wayapay.framework.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants.TABLE.SETTLEMENT
import kotlinx.android.parcel.Parcelize

data class SettlementApiResponse(
    @Expose
    val data: SettlementContent,
    @Expose
    val message: String,
    @Expose
    val status: Boolean,
    @Expose
    val timeStamp: Long
)

data class SettlementContent(
    @Expose
    val content: List<SettleContent>,
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
    val pageable: SettlementPageable,
    @Expose
    val size: Int,
    @Expose
    val sort: Sorted,
    @Expose
    val totalElements: Int,
    @Expose
    val totalPages: Int
)

data class Sorted(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)

data class Sortable(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)

data class SettlementPageable(
    @Expose
    val offset: Int,
    @Expose
    val pageNumber: Int,
    @Expose
    val pageSize: Int,
    @Expose
    val paged: Boolean,
    @Expose
    val sort: Sortable,
    @Expose
    val unpaged: Boolean
)

@Parcelize
@Entity(tableName = SETTLEMENT)
data class SettleContent(
    @Expose
    val accountSettlementOption: String,
    @Expose
    val fee: Double,
    @Expose
    val merchantId: String,
    @Expose
    val settlementBeneficiaryAccount: String,
    @Expose
    val settlementDate: String,
    @Expose
    val settlementGrossAmount: Double,
    @Expose
    val settlementNetAmount: Double,
    @PrimaryKey(autoGenerate = false)
    @Expose
    val settlementReferenceId: String,
    @Expose
    val settlementStatus: String
) : Parcelable