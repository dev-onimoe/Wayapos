package com.wayapaychat.wayapay.framework.repo.profile

import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ProfileRepo : BaseRepo {
    fun getMerchantProfile(merchantId: Int): Flow<StateMachine<APIProfileResponse>>
    fun uploadProfileImage(
        file: MultipartBody.Part,
        type: String,
        userId: Int,
    ): Flow<StateMachine<APICreateAccountResponse>>
    fun getMerchantProfileFromDb(): Flow<StateMachine<APIProfileResponse>>
    fun updateMerchantInfo(
        merchantId: Int,
        apiUpdateProfileRequest: APIUpdateProfileRequest
    ): Flow<StateMachine<APICreateAccountResponse>>
    fun updateMerchant(profileData: ProfileData): Flow<StateMachine<Long>>
}
