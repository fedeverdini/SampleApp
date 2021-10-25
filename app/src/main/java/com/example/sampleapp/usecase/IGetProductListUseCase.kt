package com.example.sampleapp.usecase

import com.example.sampleapp.model.Resource

interface IGetProductListUseCase {
    suspend fun getProductList(page: Int): Resource<List<String>>
}