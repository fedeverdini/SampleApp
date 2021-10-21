package com.example.sampleapp.ui.transaction.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.usecase.IGetTransactionListUseCase
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val getTransactionList: IGetTransactionListUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<TransactionListUiState>>()
    val uiState: LiveData<DataEvent<TransactionListUiState>>
        get() = _uiState

    fun getTransactionList(pullToRefresh: Boolean = false) {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(TransactionListUiState.Loading)
            }

            val list = getTransactionList.getTransactionList(pullToRefresh)
            when (list.status) {
                SUCCESS -> {
                    var uiState: TransactionListUiState = TransactionListUiState.ShowEmptyList
                    list.data?.let { transactionList ->
                        if (transactionList.isNotEmpty()) {
                            uiState = TransactionListUiState.AddTransactionItems(transactionList)
                        }
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }

                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            TransactionListUiState.ShowError(
                                list.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}