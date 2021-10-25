package com.example.sampleapp.usecase

import com.example.sampleapp.enum.Currency
import com.example.sampleapp.model.rate.Rate

interface IGetTransactionListUseCase {
    suspend fun generateTransactionList(rates: List<Rate>, finalCurrency: Currency = Currency.EUR)
}