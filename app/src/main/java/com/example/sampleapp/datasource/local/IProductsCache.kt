package com.example.sampleapp.datasource.local

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse

interface IProductsCache {
    fun getRateList(): Resource<List<Rate>>
    fun getTransactionList(): Resource<List<Transaction>>

    fun saveRateList(list: List<Rate>)
    fun saveProductList(list: List<Transaction>)
}