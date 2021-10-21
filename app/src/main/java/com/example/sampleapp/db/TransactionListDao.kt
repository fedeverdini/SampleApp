package com.example.sampleapp.db

import androidx.room.*
import com.example.sampleapp.model.transaction.TransactionListResponse

@Dao
interface TransactionListDao {
    @Query("SELECT * FROM Transactions ORDER BY lastUpdate DESC LIMIT 1")
    fun getTransactionList(): TransactionListResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg characters: TransactionListResponse)

    @Delete
    fun delete(characters: TransactionListResponse)

    @Query("DELETE FROM Transactions")
    fun deleteAll()
}