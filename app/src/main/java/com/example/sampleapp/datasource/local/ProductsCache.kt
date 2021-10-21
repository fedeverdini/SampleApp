package com.example.sampleapp.datasource.local

import com.example.sampleapp.db.RateListDao
import com.example.sampleapp.db.TransactionListDao
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse
import com.example.sampleapp.utils.date.IDateUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.lang.Exception
import java.util.*

class ProductsCache(
    private val transactionListDao: TransactionListDao,
    private val rateListDao: RateListDao
) : IProductsCache, KoinComponent {

    private val dateUtils: IDateUtils by inject()

    override fun getRateList(): Resource<List<Rate>> {
        return try {
            val data = rateListDao.getRateList()
            if (dateUtils.isCacheExpired(data.lastUpdate)) {
                Resource.success(null)
            } else {
                Resource.success(data.rates)
            }
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun getTransactionList(): Resource<List<Transaction>> {
        return try {
            val data = transactionListDao.getTransactionList()
            if (dateUtils.isCacheExpired(data.lastUpdate)) {
                Resource.success(null)
            } else {
                Resource.success(data.transactions)
            }
        } catch (e: Exception) {
            Resource.success(null)
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

    override fun saveProductList(list: List<Transaction>) {
        try {
            val now = Date().time
            val data = TransactionListResponse(now, list)
            transactionListDao.deleteAll()
            transactionListDao.insert(data)
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveTransactionList")
        }
    }
}