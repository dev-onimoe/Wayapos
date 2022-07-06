package com.wayapaychat.wayapay.framework.datasource.remote.profile

import com.wayapaychat.wayapay.framework.network.AuthApiService
import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import okhttp3.MultipartBody

class ProfileDataSourceImpl(
    private val apiService: AuthApiService,
    private val wayapayApiService: WayapayApiService,
) : ProfileDataSource {
    override suspend fun getMerchantInfo(merchantId: Int): APIProfileResponse {
        return apiService.profile(merchantId)
    }

    override suspend fun uploadProfileImage(
        file: MultipartBody.Part,
        type: String,
        userId: Int
    ) = apiService.updateProfileImage(file, type, userId)

    override suspend fun updateMerchantInfo(
        merchantId: Int,
        body: APIUpdateProfileRequest
    ) = apiService.updateProfile(merchantId, body)
}
