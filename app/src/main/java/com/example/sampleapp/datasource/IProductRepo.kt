package com.example.sampleapp.datasource

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse

interface IProductRepo {
    suspend fun getRateListFromCache(): Resource<List<Rate>>
    suspend fun getTransactionListFromCache(): Resource<List<Transaction>>

    suspend fun getRateListFromApi(): Resource<List<Rate>>
    suspend fun getTransactionListFromApi(): Resource<List<Transaction>>

    fun saveRateListInCache(data: List<Rate>)
    fun saveTransactionListInCache(data: List<Transaction>)
}