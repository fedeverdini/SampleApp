package com.example.sampleapp.ui.transaction.list

import com.example.sampleapp.model.error.BaseError

sealed class ProductListUiState {
    object Loading: ProductListUiState()
    data class AddProductItems(val result: List<String>): ProductListUiState()
    object ShowEmptyList: ProductListUiState()
    data class ShowError(val error: BaseError): ProductListUiState()
}