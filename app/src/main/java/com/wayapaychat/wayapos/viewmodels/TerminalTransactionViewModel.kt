package com.wayapaychat.wayapos.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapos.external.transactions.InAppTransactionApi
import com.wayapaychat.wayapos.external.transactions.TerminalTransactionApi
import com.wayapaychat.wayapos.external.ApiClient
import com.wayapaychat.wayapos.helperClasses.Constants
import com.wayapaychat.wayapos.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class TerminalTransactionViewModel: ViewModel() {

    lateinit var context : Context
    var TerminaltransactionDataList : MutableLiveData<Response>
    var InApptransactionDataList : MutableLiveData<Response>
    var walletAccountsDataList : MutableLiveData<Response>
    lateinit var cache : Cache

    init {
        TerminaltransactionDataList = MutableLiveData()
        InApptransactionDataList = MutableLiveData()
        walletAccountsDataList = MutableLiveData()
        //cache = CacheImpl(context, Gson())
    }

    fun getDataListObserver(): MutableLiveData<Response> {
        return TerminaltransactionDataList
    }
    fun getInApptransactionDataListObserver(): MutableLiveData<Response> {
        return InApptransactionDataList
    }
    fun getwalletAccountsDataListObserver(): MutableLiveData<Response> {
        return walletAccountsDataList
    }

    fun user(): APILoginResponse {

        val userDetails = cache.getObject(CacheConstants.Keys.USER_DETAILS, APILoginResponse::class.java) as APILoginResponse
        return userDetails
    }

    fun getTerminalTransactions() {

        viewModelScope.launch(Dispatchers.IO) {
            ApiClient.BASE_URL = Constants.TRANSACTION_SERVICE_BASE_URL
            val instance = ApiClient.getInstance(cache).create(TerminalTransactionApi::class.java)

            val response = instance.getTerminalTransactions(user().data?.merchantId!!)
            response.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: retrofit2.Response<Any?>) {
                    if (response.code().equals(200)) {
                        val res = Response.Success(response.body() as LinkedTreeMap<String, Any>)
                        TerminaltransactionDataList.postValue(res)
                    }else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            val message = jObjError.getString("error")
                            val res = Response.Failure(Exception(message))
                            TerminaltransactionDataList.postValue(res)

                        } catch (e: java.lang.Exception) {
                            val res = Response.Failure(e)
                            TerminaltransactionDataList.postValue(res)
                        }
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    val res = Response.Failure(Exception(t.message))
                    TerminaltransactionDataList.postValue(res)
                }
            })

        }

    }

    fun getWalletAccounts() {

        viewModelScope.launch(Dispatchers.IO) {
            ApiClient.BASE_URL = Constants.TEMPORAL_SERVICE_BASE_URL
            val instance = ApiClient.getInstance(cache).create(InAppTransactionApi::class.java)

            val response = instance.getUserWallets(user().data?.user?.id!!)
            response.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: retrofit2.Response<Any?>) {
                    if (response.code().equals(200)) {
                        val res = Response.Success(response.body() as LinkedTreeMap<String, Any>)
                        walletAccountsDataList.postValue(res)
                    }else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        try {

                            val message = jObjError.getString("error")
                            val res = Response.Failure(Exception(message))
                            walletAccountsDataList.postValue(res)

                        } catch (e: java.lang.Exception) {
                            try {
                                val message = jObjError.getString("message")
                                val res = Response.Failure(Exception(message))
                                walletAccountsDataList.postValue(res)
                            }catch(e : Exception) {
                                val res = Response.Failure(e)
                                walletAccountsDataList.postValue(res)
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    val res = Response.Failure(Exception(t.message))
                    walletAccountsDataList.postValue(res)
                }
            })

        }

    }

    fun getInAppTransactions(accountNo: String, page : Int) {

        viewModelScope.launch(Dispatchers.IO) {
            ApiClient.BASE_URL = Constants.TEMPORAL_SERVICE_BASE_URL
            val instance = ApiClient.getInstance(cache).create(InAppTransactionApi::class.java)

            val response = instance.getInAppTransactions(accountNo, page, 15)
            response.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: retrofit2.Response<Any?>) {
                    if (response.code().equals(200)) {
                        val res = Response.Success(response.body() as LinkedTreeMap<String, Any>)
                        InApptransactionDataList.postValue(res)
                    }else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        try {

                            val message = jObjError.getString("error")
                            val res = Response.Failure(Exception(message))
                            InApptransactionDataList.postValue(res)

                        } catch (e: java.lang.Exception) {
                            try {
                                val message = jObjError.getString("message")
                                val res = Response.Failure(Exception(message))
                                InApptransactionDataList.postValue(res)
                            }catch(e : Exception) {
                                val res = Response.Failure(e)
                                InApptransactionDataList.postValue(res)
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    val res = Response.Failure(Exception(t.message))
                    InApptransactionDataList.postValue(res)
                }
            })

        }

    }


}