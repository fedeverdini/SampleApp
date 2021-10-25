package com.example.sampleapp.datasource

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.ui.transaction.view.ProductView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView

interface IProductRepo {
    suspend fun getRateListFromCache(): Resource<List<Rate>>
    suspend fun getTotalAmountFromCache(sku: String): Resource<Double>
    suspend fun getProductListFromCache(page: Int): Resource<List<String>>
    suspend fun getTransactionDetailsFromCache(sku: String, page: Int): Resource<List<TransactionDetailsView>>

    suspend fun getRateListFromApi(): Resource<List<Rate>>
    suspend fun getTransactionsFromApi(): Resource<List<Transaction>>

    fun clearTransactionDetails()
    fun saveRateListInCache(data: List<Rate>)
    fun saveProductListInCache(data: List<String>)
    fun saveTransactionDetailInCache(data: TransactionDetailsView)
}