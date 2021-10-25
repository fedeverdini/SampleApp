package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils.Companion.createError

class GetTransactionDetailsUseCase(private val productRepo: IProductRepo) :
    IGetTransactionDetailsUseCase {

    override suspend fun getTransactionDetails(
        sku: String,
        page: Int
    ): Resource<List<TransactionDetailsView>> {
        val cacheData = productRepo.getTransactionDetailsFromCache(sku, page)
        return when (cacheData.status) {
            SUCCESS -> {
                return cacheData.data?.let { txnList ->
                    if (txnList.isEmpty()) {
                        Resource.success(emptyList())
                    } else {
                        Resource.success(txnList)
                    }
                } ?: run {
                    Resource.success(emptyList())
                }
            }
            ERROR -> {
                Resource.error(cacheData.error ?: createError(ErrorCode.UNKNOWN))
            }
        }
    }
}