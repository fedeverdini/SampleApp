package com.example.sampleapp.db


import androidx.room.*
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView

@Dao
interface TransactionDetailsDao {
    @Query("SELECT * FROM TransactionDetails WHERE sku = :sku LIMIT :pageSize OFFSET :page * :pageSize")
    fun getTransactionDetailsList(sku: String, page:Int, pageSize: Int): List<TransactionDetailsView>

    @Query("SELECT SUM(finalAmount) FROM TransactionDetails WHERE sku = :sku")
    fun getTotalAmount(sku: String): Double

    @Query("SELECT DISTINCT(sku) FROM TransactionDetails")
    fun getProducts(): List<String>

    @Insert
    fun insert(vararg txn: TransactionDetailsView)

    @Delete
    fun delete(txn: TransactionDetailsView)

    @Query("DELETE FROM TransactionDetails")
    fun deleteAll()
}