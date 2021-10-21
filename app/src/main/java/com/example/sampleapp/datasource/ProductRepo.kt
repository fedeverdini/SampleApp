package com.example.sampleapp.datasource

import com.example.sampleapp.model.Resource
import com.example.sampleapp.datasource.local.IProductsCache
import com.example.sampleapp.datasource.remote.ProductsApi
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse

class ProductRepo(
    private val productsApi: ProductsApi,
    private val productsCache: IProductsCache
) : IProductRepo {

    override suspend fun getRateListFromCache(): Resource<List<Rate>> {
        return productsCache.getRateList()
    }

    override suspend fun getTransactionListFromCache(): Resource<List<Transaction>> {
        return productsCache.getTransactionList()
    }

    override suspend fun getRateListFromApi(): Resource<List<Rate>> {
        return productsApi.getRates()
    }

    override suspend fun getTransactionListFromApi(): Resource<List<Transaction>> {
        return productsApi.getTransactions()
    }

    override fun saveRateListInCache(data: List<Rate>) {
        productsCache.saveRateList(data)
    }

    override fun saveTransactionListInCache(data: List<Transaction>) {
        productsCache.saveProductList(data)
    }
}