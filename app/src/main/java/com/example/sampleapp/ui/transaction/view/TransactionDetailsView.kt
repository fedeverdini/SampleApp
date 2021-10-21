package com.example.sampleapp.ui.transaction.view

data class TransactionDetailsView(
    val sku: String = "",
    val originalCurrency: String = "",
    val originalAmount: Double = 0.0,
    val finalCurrency: String = "",
    val finalAmount: Double = 0.0
)
