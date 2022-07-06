package com.wayapaychat.wayapay.framework.repo.di

import com.wayapaychat.wayapay.framework.datasource.db.dao.customer_dao.CustomerDao
import com.wayapaychat.wayapay.framework.datasource.db.dao.profile_dao.ProfileDao
import com.wayapaychat.wayapay.framework.datasource.db.dao.transaction_dao.TransactionDao
import com.wayapaychat.wayapay.framework.datasource.remote.authDataSource.AuthDatasource
import com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link.CreatePaymentLinkDatasource
import com.wayapaychat.wayapay.framework.datasource.remote.customers.CustomersDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.dispute.DisputeDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.notifications.NotificationsDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.profile.ProfileDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.subscription.SubscriptionDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSource
import com.wayapaychat.wayapay.framework.repo.auth_repo.AuthRepository
import com.wayapaychat.wayapay.framework.repo.auth_repo.AuthRepositoryImpl
import com.wayapaychat.wayapay.framework.repo.customers.CustomersRepo
import com.wayapaychat.wayapay.framework.repo.customers.CustomersRepoImpl
import com.wayapaychat.wayapay.framework.repo.dispute_repo.DisputeRepo
import com.wayapaychat.wayapay.framework.repo.dispute_repo.DisputeRepoImpl
import com.wayapaychat.wayapay.framework.repo.notification_repo.NotificationRepo
import com.wayapaychat.wayapay.framework.repo.notification_repo.NotificationsRepoImpl
import com.wayapaychat.wayapay.framework.repo.payment_link.CreatePaymentLinkRepo
import com.wayapaychat.wayapay.framework.repo.payment_link.CreatePaymentLinkRepoImpl
import com.wayapaychat.wayapay.framework.repo.profile.ProfileRepo
import com.wayapaychat.wayapay.framework.repo.profile.ProfileRepoImpl
import com.wayapaychat.wayapay.framework.repo.subscription.SubscriptionRepo
import com.wayapaychat.wayapay.framework.repo.subscription.SubscriptionRepoImpl
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepo
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun provideAuthRepo(authDatasource: AuthDatasource): AuthRepository {
        return AuthRepositoryImpl(authDatasource)
    }

    @Provides
    fun providePaymentLinkRepo(createPaymentLinkDatasource: CreatePaymentLinkDatasource): CreatePaymentLinkRepo {
        return CreatePaymentLinkRepoImpl(createPaymentLinkDatasource)
    }

    @Provides
    fun provideCustomerRepo(customersDataSource: CustomersDataSource, dao: CustomerDao): CustomersRepo {
        return CustomersRepoImpl(customersDataSource,dao)
    }

    @Provides
    fun provideSubscriptionRepo(subscriptionDataSource: SubscriptionDataSource): SubscriptionRepo {
        return SubscriptionRepoImpl(subscriptionDataSource)
    }

    @Provides
    fun provideProfileRepo(
        profileDao: ProfileDao,
        profileDataSource: ProfileDataSource
    ): ProfileRepo {
        return ProfileRepoImpl(profileDao, profileDataSource)
    }

    @Provides
    fun provideTransactionRepo(dataSource: TransactionsDataSource, dao: TransactionDao): TransactionsRepo {
        return TransactionsRepoImpl(dataSource,dao)
    }
    @Provides
    fun provideDisputeRepo(dataSource: DisputeDataSource): DisputeRepo {
        return DisputeRepoImpl(dataSource)
    }

    @Provides
    fun provideNotificationRepo(dataSource: NotificationsDataSource): NotificationRepo {
        return NotificationsRepoImpl(dataSource)
    }
}
