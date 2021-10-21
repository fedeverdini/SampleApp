package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse
import com.example.sampleapp.utils.date.IDateUtils
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils.Companion.createError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTransactionListUseCase(private val productRepo: IProductRepo) :
    IGetTransactionListUseCase {

    override suspend fun getTransactionList(pullToRefresh: Boolean): Resource<Set<String>> {
        val cacheData = if (pullToRefresh) null else productRepo.getTransactionListFromCache()
        val txnData =
            if (cacheData?.data == null) {
                productRepo.getTransactionListFromApi()
            } else {
                cacheData
            }
        return when (txnData.status) {
            SUCCESS -> {
                if (txnData.data.isNullOrEmpty()) {
                    Resource.success(emptySet())
                } else {
                    txnData.data.let {
                        productRepo.saveTransactionListInCache(txnData.data)
                    }
                    val transactionList = txnData.data.map { transaction ->
                        transaction.sku
                    }

                    val response = transactionList.toSortedSet()
                    Resource.success(response)
                }
            }
            ERROR -> {
                Resource.error(txnData.error ?: createError(ErrorCode.UNKNOWN))
            }
        }
    }
}