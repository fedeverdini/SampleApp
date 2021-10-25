package com.example.sampleapp.ui.transaction.details

import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.transaction.view.TransactionDetailsListView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import java.util.*

sealed class TransactionDetailsUiState {
    object Loading : TransactionDetailsUiState()
    data class GetRatesSuccess(val result: List<Rate>) : TransactionDetailsUiState()
    data class ShowTransactionDetails(val result: List<TransactionDetailsView>) : TransactionDetailsUiState()
    data class ShowError(val error: BaseError) : TransactionDetailsUiState()
    data class SetTotal(val amount: Double) : TransactionDetailsUiState()
    object ShowEmptyList : TransactionDetailsUiState()
}