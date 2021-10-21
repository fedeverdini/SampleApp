package com.example.sampleapp.ui.transaction.rates

import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.model.rate.Rate

sealed class RateListUiState {
    object Loading: RateListUiState()
    data class AddRateItems(val result: List<Rate>): RateListUiState()
    object ShowEmptyList: RateListUiState()
    data class ShowError(val error: BaseError): RateListUiState()
}