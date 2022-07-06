package com.wayapaychat.wayapay.framework.datasource.db.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.OtherDetails
import com.wayapaychat.wayapay.framework.network.model.ProfileData

class Converter {
    @TypeConverter
    fun convertObject(obj: OtherDetails): String = Gson().toJson(obj)

    @TypeConverter
    fun fromJsonCurrentUserInfo(currentUserInfoData: String): OtherDetails =
        Gson().fromJson(currentUserInfoData, OtherDetails::class.java)

}
