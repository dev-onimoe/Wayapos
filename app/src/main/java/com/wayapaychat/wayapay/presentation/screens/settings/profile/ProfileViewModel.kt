package com.wayapaychat.wayapay.presentation.screens.settings.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.repo.profile.ProfileRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val profileRepo: ProfileRepo,
    private val cache: Cache
) : ViewModel() {
    private val _profileObserver = MutableLiveData<StateMachine<APIProfileResponse>>()
    val profileObserver: LiveData<StateMachine<APIProfileResponse>> = _profileObserver

    private val _updateLocalProfileProfileObserver = MutableLiveData<StateMachine<Long>>()
    val updateLocalProfileProfileObserver: LiveData<StateMachine<Long>> =
        _updateLocalProfileProfileObserver

    private val _updateProfileObserver = MutableLiveData<StateMachine<APICreateAccountResponse>>()
    val updateProfileObserver: LiveData<StateMachine<APICreateAccountResponse>> =
        _updateProfileObserver

    private val _uploadProfileImageObserver =
        MutableLiveData<StateMachine<APICreateAccountResponse>>()
    val uploadProfileImageObserver: LiveData<StateMachine<APICreateAccountResponse>> =
        _uploadProfileImageObserver

    init {
        val userId =
            cache.getInt(
                CacheConstants.Keys.USER_ID
            )
        setStateEvent(
            ProfileStateEvent.FetchProfile,
            merchantId = userId
        )
    }

    fun setStateEvent(stateEvent: ProfileStateEvent, merchantId: Int, requestBody: Any? = null) {
        when (stateEvent) {
            ProfileStateEvent.FetchProfile -> {
                profileRepo.getMerchantProfile(merchantId).onEach {
                    _profileObserver.value = it
                }.launchIn(
                    viewModelScope
                )
            }
            ProfileStateEvent.FetchProfileFromDB -> {
                profileRepo.getMerchantProfileFromDb().onEach {
                    _profileObserver.value = it
                }.launchIn(

                    viewModelScope
                )
            }
            ProfileStateEvent.UpdateProfile -> {
                profileRepo.updateMerchantInfo(merchantId, requestBody as APIUpdateProfileRequest)
                    .onEach {
                        _updateProfileObserver.value = it
                    }.launchIn(
                        viewModelScope
                    )
            }
            ProfileStateEvent.UpdateProfileImage -> {
                profileRepo.uploadProfileImage(
                    requestBody as MultipartBody.Part,
                    "LEFT",
                    merchantId
                )
                    .onEach {
                        _uploadProfileImageObserver.value = it
                    }.launchIn(
                        viewModelScope
                    )
            }
            ProfileStateEvent.UpdateLocalProfile -> {
                profileRepo.updateMerchant(requestBody as ProfileData)
                    .onEach {
                        _updateLocalProfileProfileObserver.value = it
                    }.launchIn(
                        viewModelScope
                    )
            }
        }
    }
}

sealed class ProfileStateEvent {
    object FetchProfileFromDB : ProfileStateEvent()
    object FetchProfile : ProfileStateEvent()
    object UpdateProfile : ProfileStateEvent()
    object UpdateProfileImage : ProfileStateEvent()
    object UpdateLocalProfile : ProfileStateEvent()
}
