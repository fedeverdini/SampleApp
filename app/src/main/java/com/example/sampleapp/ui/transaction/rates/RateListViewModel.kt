package com.example.sampleapp.ui.transaction.rates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.usecase.IGetRateListUseCase
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RateListViewModel(
    private val getRateList: IGetRateListUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<RateListUiState>>()
    val uiState: LiveData<DataEvent<RateListUiState>>
        get() = _uiState

    fun getRateList() {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(RateListUiState.Loading)
            }

            val list = getRateList.getRates()
            when (list.status) {
                SUCCESS -> {
                    var uiState: RateListUiState = RateListUiState.ShowEmptyList
                    list.data?.let { rateList ->
                        if (rateList.isNotEmpty()) {
                            uiState = RateListUiState.AddRateItems(rateList)
                        }
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }

                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            RateListUiState.ShowError(
                                list.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}