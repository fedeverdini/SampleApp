package com.example.sampleapp.usecase

import com.example.sampleapp.enum.Currency
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.transaction.view.TransactionDetailsListView

interface IGetTransactionDetailsUseCase {
    suspend fun getTransactionDetails(
        sku: String,
        finalCurrency: Currency,
        rates: List<Rate>
    ): Resource<TransactionDetailsListView>
}