package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.model.Resource

class GetTotalAmountUseCase(private val productRepo: IProductRepo) : IGetTotalAmountUseCase {

    override suspend fun getTotalAmount(sku: String): Resource<Double> {
        return productRepo.getTotalAmountFromCache(sku)
    }
}