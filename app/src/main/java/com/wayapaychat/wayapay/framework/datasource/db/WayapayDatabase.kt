package com.wayapaychat.wayapay.framework.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wayapaychat.wayapay.framework.datasource.db.dao.customer_dao.CustomerDao
import com.wayapaychat.wayapay.framework.datasource.db.dao.profile_dao.ProfileDao
import com.wayapaychat.wayapay.framework.datasource.db.dao.transaction_dao.TransactionDao
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData

@Database(
    entities = [ProfileData::class, TransactionData::class, SettleContent::class, CustomerContent::class],
    version = 15
)
abstract class WayapayDatabase : RoomDatabase() {
    abstract fun getMerchantDao(): ProfileDao
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCustomerDao(): CustomerDao
}
