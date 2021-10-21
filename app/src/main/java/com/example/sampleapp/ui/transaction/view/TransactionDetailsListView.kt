package com.example.sampleapp.ui.transaction.view

import com.example.sampleapp.enum.Currency

data class TransactionDetailsListView(
    val total: Double,
    val currency: Currency,
    val transactions: List<TransactionDetailsView>,
    val conversionRateError: Boolean = false
)
