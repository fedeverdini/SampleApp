package com.example.sampleapp.ui.transaction.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.usecase.IGetTotalAmountUseCase
import com.example.sampleapp.usecase.IGetTransactionDetailsUseCase
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(
    private val getTransactionDetailsUseCase: IGetTransactionDetailsUseCase,
    private val getTotalAmountUseCase: IGetTotalAmountUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<TransactionDetailsUiState>>()
    val uiState: LiveData<DataEvent<TransactionDetailsUiState>>
        get() = _uiState

    fun getTransactionDetails(sku: String, page: Int) {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(TransactionDetailsUiState.Loading)
            }

            val details = getTransactionDetailsUseCase.getTransactionDetails(sku, page)
            when (details.status) {
                SUCCESS -> {
                    var uiState: TransactionDetailsUiState =
                        TransactionDetailsUiState.ShowEmptyList

                    details.data?.let {
                        if (it.isNotEmpty()) {
                            uiState = TransactionDetailsUiState.ShowTransactionDetails(it)
                        }
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }
                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            TransactionDetailsUiState.ShowError(
                                details.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }

    fun getTotalAmount(sku: String) {
        ioScope.launch {
            val details = getTotalAmountUseCase.getTotalAmount(sku)
            when (details.status) {
                SUCCESS -> {
                    details.data?.let {
                        mainScope.launch {
                            _uiState.value = DataEvent(TransactionDetailsUiState.SetTotal(it))
                        }
                    }
                }
                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            TransactionDetailsUiState.ShowError(
                                details.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}