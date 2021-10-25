package com.example.sampleapp.datasource.local

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.transaction.view.ProductView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView

interface IProductsCache {
    fun getRateList(): Resource<List<Rate>>
    fun getTotalAmount(sku: String): Resource<Double>
    fun getProductList(page: Int): Resource<List<String>>
    fun getTransactionList(sku: String, page: Int): Resource<List<TransactionDetailsView>>

    fun clearTransactionDetails()
    fun saveRateList(list: List<Rate>)
    fun saveProductList(list: List<String>)
    fun saveTransactionDetail(data: TransactionDetailsView)
}