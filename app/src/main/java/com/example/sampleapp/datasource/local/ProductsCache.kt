package com.example.sampleapp.datasource.local

import com.example.sampleapp.db.RateListDao
import com.example.sampleapp.db.ProductsDao
import com.example.sampleapp.db.TransactionDetailsDao
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.ui.transaction.view.ProductView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.constants.Constants
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.lang.Exception
import java.util.*

class ProductsCache(
    private val transactionDetailsDao: TransactionDetailsDao,
    private val productsDao: ProductsDao,
    private val rateListDao: RateListDao
) : IProductsCache, KoinComponent {

    override fun getRateList(): Resource<List<Rate>> {
        return try {
            val data = rateListDao.getRateList()
            Resource.success(data.rates)
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun getProductList(page: Int): Resource<List<String>> {
        return try {
            val data = productsDao.getProductList(page, Constants.PRODUCT_LIST_PAGE_SIZE)
            Resource.success(data)
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun getTransactionList(sku: String, page: Int): Resource<List<TransactionDetailsView>> {
        return try {
            val data = transactionDetailsDao.getTransactionDetailsList(sku, page, Constants.TRANSACTION_DETAILS_PAGE_SIZE)
            Resource.success(data)
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun getTotalAmount(sku: String): Resource<Double> {
        return try {
            val data = transactionDetailsDao.getTotalAmount(sku)
            Resource.success(data)
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun saveProductList(list: List<String>) {
        try {
            productsDao.deleteAll()
            list.forEach { product ->
                productsDao.insert(ProductView(product))
            }
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveProductList")
        }
    }

    override fun saveRateList(list: List<Rate>) {
        try {
            val now = Date().time
            val data = RateListResponse(now, list)
            rateListDao.deleteAll()
            rateListDao.insert(data)
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveRateList")
        }
    }

    override fun clearTransactionDetails() {
        try {
            transactionDetailsDao.deleteAll()
        } catch (e: Exception) {
            Timber.d("CACHE CLEAR FAILED: deleteAll from TransactionDetails")
        }
    }

    override fun saveTransactionDetail(data: TransactionDetailsView) {
        try {
            transactionDetailsDao.insert(data)
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveTransactionDetail")
        }
    }
}