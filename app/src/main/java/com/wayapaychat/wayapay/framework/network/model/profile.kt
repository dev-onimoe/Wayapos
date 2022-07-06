package com.wayapaychat.wayapay.framework.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wayapaychat.wayapay.framework.datasource.db.type_converter.Converter
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants.TABLE.MERCHANT


data class APIProfileResponse(
    @Expose
    val data: ProfileData?,
    @Expose
    val message: String?,
    @Expose
    val status: Boolean?,
    @Expose
    val timestamp: String?
)

@TypeConverters(Converter::class)
@Entity(tableName = MERCHANT)
data class ProfileData(
    @Expose
    val corporate: Boolean? = false,
    @Expose
    val dateOfBirth: String? = "",
    @Expose
    val email: String? = "",
    @Expose
    val firstName: String? = "",
    @Expose
    val middleName: String? = "",
    @Expose
    val gender: String? = "",
    @Expose
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    @Expose
    val otherDetails: OtherDetails,
    @Expose
    val phoneNumber: String? = "",
    @Expose
    val district: String? = "",
    @Expose
    val address: String? = "",
    @Expose
    val referral: String? = "",
    @Expose
    val city: String? = "",
    @Expose
    val referenceCode: String? = "",
    @Expose
    val smsAlertConfig: Boolean? = false,
    @Expose
    val surname: String? = "",
    @Expose
    var profileImage: String? = "",
    @Expose
    var banKName: String? = "",
    @Expose
    var bankCode: String? = "",
    @Expose
    var accountNumber: String? = "",
    @Expose
    var accountName: String? = "",
    @Expose
    var userId: Int? = 0,
    @Expose
    var referalCode: String? = "",
    @Expose
    var isEmailVerified: Boolean? = false,
    @Expose
    var isPhoneVerified: Boolean? = false,
) {
}


data class OtherDetails(
    @Expose
    val businessType: String? = "",
    @Expose
    val organisationEmail: String? = "",
    @Expose
    val organisationName: String? = "",
    @Expose
    val organisationPhone: String? = "",
    @Expose
    val organisationType: String? = "",
    @Expose
    val organizationAddress: String? = "",
    @Expose
    val organizationCity: String? = "",
    @Expose
    val organizationState: String? = ""
)

data class APIUpdateProfileRequest(
    @Expose
    val address: String,
    @Expose
    val organisationName: String,
    @Expose
    val businessType: String,
    @Expose
    val organisationType: String,
    @Expose
    val city: String,
    @Expose
    val dateOfBirth: String,
    @Expose
    val district: String,
    @Expose
    val email: String,
    @Expose
    val state: String,
    @Expose
    val surname: String,
    @Expose
    val firstName: String,
    @Expose
    val gender: String,
    @Expose
    val middleName: String,
    @Expose
    val phoneNumber: String
)

data class APIAccountNumberResponse(
    @Expose
    val timestamp: String?,
    @Expose
    val status: Boolean?,
    @Expose
    val message: String?,
    @Expose
    val data: AccountNumberData?
)

data class AccountNumberData(
    @Expose
    val banKName: String?,
    @Expose
    val bankCode: String?,
    @Expose
    val accountNumber: String?,
    @Expose
    val accountName: String?,
    @Expose
    val userId: String?,
    @Expose
    val deleted: Boolean?,
    @Expose
    var referalCode: String
)
