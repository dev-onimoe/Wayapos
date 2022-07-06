package com.wayapaychat.wayapay.presentation.utils.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object CacheConstants {
    object Keys {
        const val GLOBAL_CACHE_KEY = "WAYAPAY_APP"
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val LOGIN_DETAILS = "LOGIN_DETAILS"
        const val USER_DETAILS = "USER_DETAILS"
        const val MERCHANT_ID = "MERCHANT_ID"
        const val USER_ID = "USER_ID"
        const val ACCOUNT_NUMBER = "ACCOUNT_NUMBER"
        const val TOKEN = "TOKEN"
        const val FIRST_LOGIN = "FIRST_LOGIN"
    }
}

class CacheImpl(private val applicationContext: Context, private val gson: Gson) :
    Cache {
    lateinit var sharedPreferences: SharedPreferences

    private fun getInstance(): SharedPreferences {
        return applicationContext.applicationContext.getSharedPreferences(
            CacheConstants.Keys.GLOBAL_CACHE_KEY,
            Context.MODE_PRIVATE
        )
    }

    override fun putString(key: String, value: String) =
        getInstance().edit().putString(key, value).commit()

    override fun getString(key: String) = getInstance().getString(key, "")!!
    override fun putLong(key: String, value: Long) =
        getInstance().edit().putLong(key, value).commit()

    override fun getLong(key: String) = getInstance().getLong(key, -1)
    override fun putFloat(key: String, value: Float) =
        getInstance().edit().putFloat(key, value).commit()

    override fun getFloat(key: String) = getInstance().getFloat(key, -1f)
    override fun putInt(key: String, value: Int) = getInstance().edit().putInt(key, value).commit()
    override fun getInt(key: String) = getInstance().getInt(key, -1)
    override fun putDouble(key: String, value: Double) =
        getInstance().edit().putFloat(key, value.toFloat()).commit()

    override fun getDouble(key: String) = getInstance().getFloat(key, -1f).toDouble()
    override fun putObject(key: String, value: Any) = putString(key, gson.toJson(value))
    override fun getObject(key: String, classK: Class<*>): Any? {
        val string = getString(key)
        var obj: Any? = null
        if (string != "-1") {
            obj = gson.fromJson(string, classK) as Any
        }
        return obj
    }

    override fun remove(key: String) {
        getInstance().edit().remove(key).apply()
    }

    override fun containKey(key: String) = getInstance().contains(key)
    override fun clear() {
        val preferences = getInstance()
        val dataMap = entries()
        val editor = preferences.edit()
        for (key in dataMap.keys) {
            editor.remove(key)
        }
        editor.apply()
    }

    override fun entries(): Map<String?, *> {
        var map: Map<String?, *> = HashMap<String?, Any>()
        val preferences = getInstance()
        map = preferences.all
        return map
    }

    override fun putBoolean(key: String, value: Boolean) =
        getInstance().edit().putBoolean(key, value).commit()

    override fun getBoolean(key: String) = getInstance().getBoolean(key, false)
}
