package com.example.sampleapp.usecase

import com.example.sampleapp.model.Resource

interface IGetTotalAmountUseCase {
    suspend fun getTotalAmount(sku: String): Resource<Double>
}