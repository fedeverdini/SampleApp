package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils.Companion.createError

class GetProductListUseCase(private val productRepo: IProductRepo) :
    IGetProductListUseCase {

    override suspend fun getProductList(page: Int): Resource<List<String>> {
        val cacheData = productRepo.getProductListFromCache(page)
        return when (cacheData.status) {
            SUCCESS -> {
                if (cacheData.data.isNullOrEmpty()) {
                    Resource.success(emptyList())
                } else {
                    Resource.success(cacheData.data.toList())
                }
            }
            ERROR -> {
                Resource.error(cacheData.error ?: createError(ErrorCode.UNKNOWN))
            }
        }
    }
}