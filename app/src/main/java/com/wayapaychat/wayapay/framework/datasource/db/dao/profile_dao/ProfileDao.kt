package com.wayapaychat.wayapay.framework.datasource.db.dao.profile_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.wayapay.framework.network.model.LoginData
import com.wayapaychat.wayapay.framework.network.model.ProfileData

@Dao
interface ProfileDao {
    @Query("SELECT * FROM merchant")
    suspend fun getMerchantInfo(): ProfileData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun merchant(profileData: ProfileData?): Long
}
