package com.wayapaychat.wayapay.framework.datasource.remote.profile

import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import okhttp3.MultipartBody

interface ProfileDataSource {
    suspend fun getMerchantInfo(merchantId: Int): APIProfileResponse
    suspend fun uploadProfileImage(
        file: MultipartBody.Part,
        type: String,
        userId: Int,
    ): APICreateAccountResponse

    suspend fun updateMerchantInfo(
        merchantId: Int,
        body: APIUpdateProfileRequest
    ): APICreateAccountResponse
}
