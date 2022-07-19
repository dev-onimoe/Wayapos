package com.wayapaychat.wayapos.helperClasses

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LocalCache (var name : String, var payload : HashMap<String, Any>? = null, var context : Context){

    val prefs: SharedPreferences = (context as AppCompatActivity).getSharedPreferences(name, MODE_PRIVATE)
    val gson = Gson()

    fun save() {

        val stringPayload = gson.toJson(payload)
        prefs.edit().putString(name, stringPayload).apply()
    }

    fun retrieve() : HashMap<String, Any> {
        val storedHashMapString : String? = prefs.getString(name, "Error")
        val type: Type = object : TypeToken<HashMap<String?, String?>?>() {}.getType()
        val testHashMap2: HashMap<String, Any> = gson.fromJson(storedHashMapString, type)
        return testHashMap2
    }

}