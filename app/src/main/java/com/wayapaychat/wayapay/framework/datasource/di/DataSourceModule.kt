package com.wayapaychat.wayapay.framework.datasource.di

import com.wayapaychat.wayapay.framework.datasource.remote.authDataSource.AuthDatasource
import com.wayapaychat.wayapay.framework.datasource.remote.authDataSource.AuthDatasourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link.CreatePaymentLinkDatasource
import com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link.CreatePaymentLinkDatasourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.customers.CustomersDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.customers.CustomersDataSourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.dispute.DisputeDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.dispute.DisputeDataSourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.notifications.NotificationsDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.notifications.NotificationsDataSourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.profile.ProfileDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.profile.ProfileDataSourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.subscription.SubscriptionDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.subscription.SubscriptionDataSourceImpl
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSourceImpl
import com.wayapaychat.wayapay.framework.network.AuthApiService
import com.wayapaychat.wayapay.framework.network.DisputeApiService
import com.wayapaychat.wayapay.framework.network.NotificationsApiService
import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.WayapayTransactionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideAuthDataSource(authApiService: AuthApiService): AuthDatasource {
        return AuthDatasourceImpl(authApiService)
    }

    @Provides
    fun providePaymentLinkDatasource(apiService: WayapayApiService): CreatePaymentLinkDatasource {
        return CreatePaymentLinkDatasourceImpl(apiService)
    }

    @Provides
    fun provideCustomerDatasource(apiService: WayapayApiService,api: WayapayTransactionApiService): CustomersDataSource {
        return CustomersDataSourceImpl(apiService,api)
    }

    @Provides
    fun provideSubscriptionDatasource(apiService: WayapayApiService): SubscriptionDataSource {
        return SubscriptionDataSourceImpl(apiService)
    }

    @Provides
    fun provideProfileDatasource(
        apiService: AuthApiService,
        wayapayApiService: WayapayApiService
    ): ProfileDataSource {
        return ProfileDataSourceImpl(apiService, wayapayApiService)
    }


    @Provides
    fun provideTransactionsDatasource(apiService: WayapayTransactionApiService): TransactionsDataSource {
        return TransactionsDataSourceImpl(apiService)
    }

    @Provides
    fun provideDisputeDatasource(apiService: DisputeApiService): DisputeDataSource {
        return DisputeDataSourceImpl(apiService)
    }

    @Provides
    fun provideNotificationDatasource(apiService: NotificationsApiService): NotificationsDataSource {
        return NotificationsDataSourceImpl(apiService)
    }

}
