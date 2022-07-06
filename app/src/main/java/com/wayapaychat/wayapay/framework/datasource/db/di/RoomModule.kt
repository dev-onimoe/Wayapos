package com.wayapaychat.wayapay.framework.datasource.db.di

import android.content.Context
import androidx.room.Room
import com.wayapaychat.wayapay.framework.datasource.db.WayapayDatabase
import com.wayapaychat.wayapay.presentation.utils.constants.DbConstants.WAYAPAY_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): WayapayDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WayapayDatabase::class.java,
            WAYAPAY_DATABASE
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideMerchantDao(wayapayDatabase: WayapayDatabase) = wayapayDatabase.getMerchantDao()

    @Provides
    @Singleton
    fun provideTransactionsDao(wayapayDatabase: WayapayDatabase) = wayapayDatabase.getTransactionDao()

    @Provides
    @Singleton
    fun provideCustomerDao(wayapayDatabase: WayapayDatabase) = wayapayDatabase.getCustomerDao ()
}
