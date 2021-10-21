package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.utils.date.DateUtils
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRateListUseCase(private val productRepo: IProductRepo) : IGetRateListUseCase {

    override suspend fun getRates(): Resource<List<Rate>> {
        val cacheData = productRepo.getRateListFromCache()
        val rateData =
            if (cacheData.data == null) {
                productRepo.getRateListFromApi()
            } else {
                cacheData
            }
        return when (rateData.status) {
            Resource.Status.SUCCESS -> {
                rateData.data?.let {
                    if (it.isEmpty()) {
                        Resource.error(rateData.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN))
                    } else {
                        productRepo.saveRateListInCache(it)
                        Resource.success(it)
                    }
                } ?: run {
                    Resource.error(rateData.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN))
                }
            }
            Resource.Status.ERROR -> {
                Resource.error(rateData.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN))
            }
        }
    }
}