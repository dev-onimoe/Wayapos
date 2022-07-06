package com.wayapaychat.wayapay.presentation.utils.cache

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ObjectSerializerModule {
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context, gson: Gson): Cache {
        return CacheImpl(context, gson)
    }
}
