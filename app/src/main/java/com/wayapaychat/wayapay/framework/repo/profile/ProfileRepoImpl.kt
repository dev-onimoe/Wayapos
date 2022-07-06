package com.wayapaychat.wayapay.framework.repo.profile

import com.wayapaychat.wayapay.framework.datasource.db.dao.profile_dao.ProfileDao
import com.wayapaychat.wayapay.framework.datasource.remote.profile.ProfileDataSource
import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class ProfileRepoImpl(
    private val profileDao: ProfileDao,
    private val profileDataSource: ProfileDataSource
) : ProfileRepo {

    override fun getMerchantProfile(merchantId: Int) =
        flow<StateMachine<APIProfileResponse>> {
            emit(StateMachine.Loading)
            try {
                val dbResponse: ProfileData? = profileDao.getMerchantInfo()
                if (dbResponse == null) {
                    emit(
                        StateMachine.Success(
                            fetchAndSaveToDb(
                                merchantId
                            )
                        )
                    )
                } else {
                    val apiProfileResponse = APIProfileResponse(
                        status = null,
                        timestamp = null,
                        message = null,
                        data = dbResponse
                    )
                    emit(StateMachine.Success(apiProfileResponse))
                    emit(StateMachine.Success(fetchAndSaveToDb(merchantId)))
                }
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

   /* override fun getMerchantProfile(merchantId: Int) =
        flow<StateMachine<APIProfileResponse>> {
            emit(StateMachine.Loading)
            try {
                emit(
                    StateMachine.Success(
                        profileDataSource.getMerchantInfo(merchantId)
                    )
                )
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }*/

    override fun getMerchantProfileFromDb() = flow<StateMachine<APIProfileResponse>> {
        emit(StateMachine.Loading)
        try {
            val apiProfileResponse = APIProfileResponse(
                status = null,
                timestamp = null,
                message = null,
                data = profileDao.getMerchantInfo()
            )
            emit(StateMachine.Success(apiProfileResponse))
        } catch (e: Exception) {
            emit(StateMachine.Error(e))
        }
    }

    override fun updateMerchantInfo(
        merchantId: Int,
        apiUpdateProfileRequest: APIUpdateProfileRequest
    ) = flow<StateMachine<APICreateAccountResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = profileDataSource.updateMerchantInfo(merchantId, apiUpdateProfileRequest)
            emit(StateMachine.Success(response))
        } catch (e: Throwable) {
            emitError(this, e)
        }
    }

    private suspend fun fetchAndSaveToDb(merchantId: Int): APIProfileResponse {

        val response = profileDataSource.getMerchantInfo(merchantId)
        profileDao.merchant(
            profileData = response.data
        )
        val dbResponse = profileDao.getMerchantInfo()

        return APIProfileResponse(
            status = response.status,
            timestamp = response.timestamp,
            message = response.message,
            data = dbResponse
        )
    }

    override fun updateMerchant(profileData: ProfileData) = flow<StateMachine<Long>> {
        emit(StateMachine.Loading)
        try {
            val response = profileDao.merchant(profileData)
            emit(StateMachine.Success(response))
        } catch (e: Exception) {
            emit(StateMachine.Error(e))
        }
    }

    override fun uploadProfileImage(
        file: MultipartBody.Part,
        type: String,
        userId: Int
    ) = flow<StateMachine<APICreateAccountResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = profileDataSource.uploadProfileImage(file, type, userId)
            emit(StateMachine.Success(response))
        } catch (e: Throwable) {
            emitError(this, e)
        }
    }
}
