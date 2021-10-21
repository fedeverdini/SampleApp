package com.example.sampleapp.model.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transactions")
data class TransactionListResponse(
    @PrimaryKey
    var lastUpdate: Long = 0,
    val transactions: List<Transaction>
)