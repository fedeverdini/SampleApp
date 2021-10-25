package com.example.sampleapp.usecase

import com.example.sampleapp.model.Resource
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView

interface IGetTransactionDetailsUseCase {
    suspend fun getTransactionDetails(
        sku: String,
        page: Int
    ): Resource<List<TransactionDetailsView>>
}