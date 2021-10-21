package com.example.sampleapp.usecase

import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.rate.Rate

interface IGetRateListUseCase {
    suspend fun getRates(): Resource<List<Rate>>
}