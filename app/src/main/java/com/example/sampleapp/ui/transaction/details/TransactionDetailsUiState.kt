package com.example.sampleapp.ui.transaction.details

import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.transaction.view.TransactionDetailsListView

sealed class TransactionDetailsUiState {
    object Loading : TransactionDetailsUiState()
    data class GetRatesSuccess(val result: List<Rate>) : TransactionDetailsUiState()
    data class ShowTransactionDetails(val result: TransactionDetailsListView) : TransactionDetailsUiState()
    data class ShowError(val error: BaseError) : TransactionDetailsUiState()
    object ShowEmptyList : TransactionDetailsUiState()
}