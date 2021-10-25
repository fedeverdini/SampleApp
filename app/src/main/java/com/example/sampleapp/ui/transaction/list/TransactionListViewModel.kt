package com.example.sampleapp.ui.transaction.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.usecase.IGetProductListUseCase
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val getProductListUseCase: IGetProductListUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<ProductListUiState>>()
    val uiState: LiveData<DataEvent<ProductListUiState>>
        get() = _uiState

    fun getProductList(page: Int) {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(ProductListUiState.Loading)
            }

            val list = getProductListUseCase.getProductList(page)
            when (list.status) {
                SUCCESS -> {
                    var uiState: ProductListUiState = ProductListUiState.ShowEmptyList
                    list.data?.let { transactionList ->
                        if (transactionList.isNotEmpty()) {
                            uiState = ProductListUiState.AddProductItems(transactionList)
                        }
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }

                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            ProductListUiState.ShowError(
                                list.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}