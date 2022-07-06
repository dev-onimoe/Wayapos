package com.wayapaychat.wayapay.presentation.utils.cache

import java.io.Serializable

interface Cache : Serializable {

    fun putString(key: String, value: String): Boolean
    fun getString(key: String): String

    fun putBoolean(key: String, value: Boolean): Boolean
    fun getBoolean(key: String): Boolean

    fun putLong(key: String, value: Long): Boolean
    fun getLong(key: String): Long

    fun putFloat(key: String, value: Float): Boolean
    fun getFloat(key: String): Float

    fun putInt(key: String, value: Int): Boolean
    fun getInt(key: String): Int

    fun putDouble(key: String, value: Double): Boolean
    fun getDouble(key: String): Double

    fun putObject(key: String, value: Any): Boolean
    fun getObject(key: String, classK: Class<*>): Any?

    fun remove(key: String)

    fun containKey(key: String): Boolean

    fun entries(): Map<String?, *>

    fun clear()
}
