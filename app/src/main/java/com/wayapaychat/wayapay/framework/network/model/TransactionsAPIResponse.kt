package com.wayapaychat.wayapay.framework.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants.TABLE.TRANSACTION
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class TransactionsAPIResponse(
    @Expose
    val data: List<TransactionData>,
    @Expose
    val message: String,
    @Expose
    val status: Boolean,
    @Expose
    val timeStamp: Long
)

@Parcelize
@Entity(tableName = TRANSACTION)
data class TransactionData(
    @Expose
    val amount: Double,
    @Expose
    val channel: String?,
    @Expose
    val customerEmail: String?,
    @Expose
    val customerId: String?,
    @Expose
    val customerIpAddress: String?,
    @Expose
    val customerName: String,
    @Expose
    val customerPhone: String?,
    @Expose
    val fee: Double?,
    @Expose
    val maskedPan: String?,
    @Expose
    val merchantId: String?,
    @Expose
    val paymentMetaData: String?,
    @Expose
    val rcre_time: String?,
    @Expose
    @PrimaryKey(autoGenerate = false)
    val refNo: String,
    @Expose
    val scheme: String?,
    @Expose
    val status: String?,
    @Expose
    val tranDate: String?,
    @Expose
    val vendorDate: String?
) : Parcelable {

}