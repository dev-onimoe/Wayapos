package com.wayapaychat.wayapay.framework.datasource.db.dao.transaction_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData

@Dao
interface TransactionDao {
    @Query("SELECT * from `transaction` WHERE status LIKE :myStatus AND channel LIKE :mychannel AND tranDate LIKE :date AND refNo LIKE :search")
    suspend fun getTransactions(myStatus: String, mychannel: String, date : String,search: String): List<TransactionData>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTransaction(transactionData: TransactionData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettlement(transactionData: SettleContent): Long

    @Query("SELECT * from `settlement` WHERE settlementStatus LIKE :myStatus AND settlementBeneficiaryAccount LIKE :mychannel AND settlementDate LIKE :date AND settlementReferenceId LIKE :search")
    suspend fun getSettlements(myStatus: String, mychannel: String, date : String,search: String): List<SettleContent>?

}