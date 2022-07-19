package com.wayapaychat.wayapos.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapos.external.transactions.TerminalTransactionApi
import com.wayapaychat.wayapos.external.ApiClient
import com.wayapaychat.wayapos.external.UserMerchant.UserMerchant
import com.wayapaychat.wayapos.helperClasses.Constants
import com.wayapaychat.wayapos.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class WayaposHomeViewModel: ViewModel() {

    lateinit var context : Context
    var MerchantsDataList : MutableLiveData<Response>
    lateinit var cache : Cache

    init {
        MerchantsDataList = MutableLiveData()

    }

    fun getMerchantListObserver(): MutableLiveData<Response> {
        return MerchantsDataList
    }

    fun merchantList() {

        viewModelScope.launch(Dispatchers.IO) {
            ApiClient.BASE_URL = Constants.AGENCY_SERVICE_BASE_URL
            val instance = ApiClient.getInstance(cache).create(UserMerchant::class.java)

            val response = instance.getMerchants()
            response.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: retrofit2.Response<Any?>) {
                    if (response.code().equals(200)) {
                        val res = Response.Success(response.body() as LinkedTreeMap<String, Any>)
                        MerchantsDataList.postValue(res)
                    }else {
                        try {
                            if (response.errorBody()!!.toString() != null) {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                val message = jObjError.getString("error")
                                val res = Response.Failure(Exception(message))
                                MerchantsDataList.postValue(res)
                            }else {
                                val res = Response.Failure(Exception("Something went wrong"))
                                MerchantsDataList.postValue(res)
                            }

                        } catch (e: java.lang.Exception) {
                            val res = Response.Failure(e)
                            MerchantsDataList.postValue(res)
                        }
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    val res = Response.Failure(Exception(t.message))
                    MerchantsDataList.postValue(res)
                }
            })

        }

    }
}