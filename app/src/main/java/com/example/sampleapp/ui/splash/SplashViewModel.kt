package com.example.sampleapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.enum.Currency
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.usecase.IGetRateListUseCase
import com.example.sampleapp.usecase.IGetTransactionListUseCase
import com.example.sampleapp.utils.preferences.IPreferenceUtils
import com.example.sampleapp.utils.preferences.PreferenceUtils.Companion.FINAL_CURRENCY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getTransactionListUseCase: IGetTransactionListUseCase,
    private val getRateListUseCase: IGetRateListUseCase,
    private val preferenceUtils: IPreferenceUtils,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val uiScope = CoroutineScope(dispatcher.Main)

    private var _ratesLiveData: MutableLiveData<List<Rate>> = MutableLiveData()
    val ratesLiveData: LiveData<List<Rate>> = _ratesLiveData

    fun getTransaction(rates: List<Rate>) {
        ioScope.launch {
            if (rates.isNotEmpty()) {
                preferenceUtils.putString(FINAL_CURRENCY, Currency.EUR.name)
                getTransactionListUseCase.generateTransactionList(rates, Currency.EUR)
            }
        }
    }

    fun getRates() {
        ioScope.launch {
            val rateList = getRateListUseCase.getRates()
            uiScope.launch {
                _ratesLiveData.value = when (rateList.status) {
                    SUCCESS -> {
                        rateList.data ?: emptyList()
                    }
                    ERROR -> {
                        emptyList()
                    }
                }
            }
        }
    }
}