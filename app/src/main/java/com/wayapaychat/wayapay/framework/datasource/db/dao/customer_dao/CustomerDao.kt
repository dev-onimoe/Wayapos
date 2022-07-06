package com.wayapaychat.wayapay.framework.datasource.db.dao.customer_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData

@Dao
interface CustomerDao {
    @Query("SELECT * from `customer` WHERE status LIKE :myStatus AND customerId  LIKE :search ")
    suspend fun getCustomers(myStatus: String, search: String): List<CustomerContent>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomer(customerData: CustomerContent): Long

}