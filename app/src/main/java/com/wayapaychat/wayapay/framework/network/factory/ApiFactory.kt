package com.wayapaychat.wayapay.framework.network.factory

import retrofit2.Retrofit

interface ApiFactory {
    fun createRetrofit(url: String, isDebug: Boolean): Retrofit
}
