package com.example.sampleapp.ui.transaction.list

import com.example.sampleapp.model.error.BaseError

sealed class TransactionListUiState {
    object Loading: TransactionListUiState()
    data class AddTransactionItems(val result: Set<String>): TransactionListUiState()
    object ShowEmptyList: TransactionListUiState()
    data class ShowError(val error: BaseError): TransactionListUiState()
}