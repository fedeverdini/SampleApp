package com.example.sampleapp.ui.transaction.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TransactionDetails")
data class TransactionDetailsView(
    val sku: String = "",
    val originalCurrency: String = "",
    val originalAmount: Double = 0.0,
    val finalCurrency: String = "",
    val finalAmount: Double = 0.0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
