package com.example.sampleapp.datasource

import com.example.sampleapp.model.Resource
import com.example.sampleapp.datasource.local.IProductsCache
import com.example.sampleapp.datasource.remote.ProductsApi
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView

class ProductRepo(
    private val productsApi: ProductsApi,
    private val productsCache: IProductsCache
) : IProductRepo {

    override suspend fun getRateListFromCache(): Resource<List<Rate>> {
        return productsCache.getRateList()
    }

    override suspend fun getTransactionDetailsFromCache(
        sku: String,
        page: Int
    ): Resource<List<TransactionDetailsView>> {
        return productsCache.getTransactionList(sku, page)
    }

    override suspend fun getRateListFromApi(): Resource<List<Rate>> {
        return productsApi.getRates()
    }

    override suspend fun getTransactionsFromApi(): Resource<List<Transaction>> {
        return productsApi.getTransactions()
    }

    override suspend fun getProductListFromCache(page: Int): Resource<List<String>> {
        return productsCache.getProductList(page)
    }

    override suspend fun getTotalAmountFromCache(sku: String): Resource<Double> {
        return productsCache.getTotalAmount(sku)
    }

    override fun saveRateListInCache(data: List<Rate>) {
        productsCache.saveRateList(data)
    }

    override fun clearTransactionDetails() {
        productsCache.clearTransactionDetails()
    }

    override fun saveTransactionDetailInCache(data: TransactionDetailsView) {
        productsCache.saveTransactionDetail(data)
    }

    override fun saveProductListInCache(data: List<String>) {
        productsCache.saveProductList(data)
    }
}