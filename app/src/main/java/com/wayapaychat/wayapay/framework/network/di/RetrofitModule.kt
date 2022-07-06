package com.wayapaychat.wayapay.framework.network.di

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.wayapaychat.wayapay.BuildConfig
import com.wayapaychat.wayapay.framework.network.AuthApiService
import com.wayapaychat.wayapay.framework.network.DisputeApiService
import com.wayapaychat.wayapay.framework.network.NotificationsApiService
import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.WayapayTransactionApiService
import com.wayapaychat.wayapay.framework.network.factory.ApiFactory
import com.wayapaychat.wayapay.framework.network.factory.RetrofitApiFactory
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideApiService(apiFactory: ApiFactory): AuthApiService {
        return apiFactory.createRetrofit(
            url = ApiConstants.BASE_URL,
            isDebug = BuildConfig.DEBUG,
        ).create(AuthApiService::class.java)
    }

    @Provides
    fun provideDisputeApiService(apiFactory: ApiFactory): DisputeApiService {
        return apiFactory.createRetrofit(
            url = ApiConstants.WAYAPAY_DISPUTE_BASE_URL,
            isDebug = BuildConfig.DEBUG,
        ).create(DisputeApiService::class.java)
    }

    @Provides
    fun provideWayapayService(apiFactory: ApiFactory): WayapayApiService {
        return apiFactory.createRetrofit(
            url = ApiConstants.WAYAPAY_BASE_URL,
            isDebug = BuildConfig.DEBUG
        ).create(WayapayApiService::class.java)
    }

    @Provides
    fun provideWayapayTransactionService(apiFactory: ApiFactory): WayapayTransactionApiService {
        return apiFactory.createRetrofit(
            url = ApiConstants.WAYAPAY_PAYMENT_BASE_URL,
            isDebug = BuildConfig.DEBUG
        ).create(WayapayTransactionApiService::class.java)
    }

    @Provides
    fun provideNotificationService(apiFactory: ApiFactory): NotificationsApiService {
        return apiFactory.createRetrofit(
            url = ApiConstants.WAYAPAY_NOTIFICATIONS_BASE_URL,
            isDebug = BuildConfig.DEBUG
        ).create(NotificationsApiService::class.java)
    }

    @Provides
    fun factory(
        moshi: Moshi,
        gson: Gson,
        cache: Cache
    ): ApiFactory =
        RetrofitApiFactory(moshi, gson, cache)
}
