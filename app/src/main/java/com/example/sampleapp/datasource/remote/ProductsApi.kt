package com.example.sampleapp.datasource.remote

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.utils.constants.NetworkUrl.GET_TRANSACTIONS
import com.example.sampleapp.utils.constants.NetworkUrl.GET_RATES
import retrofit2.http.GET

interface ProductsApi {
    @GET(GET_RATES)
    suspend fun getRates(): Resource<List<Rate>>

    @GET(GET_TRANSACTIONS)
    suspend fun getTransactions(): Resource<List<Transaction>>
}