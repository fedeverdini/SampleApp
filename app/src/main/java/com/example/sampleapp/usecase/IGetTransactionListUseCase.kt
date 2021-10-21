package com.example.sampleapp.usecase

import com.example.sampleapp.model.Resource

interface IGetTransactionListUseCase {
    suspend fun getTransactionList(pullToRefresh: Boolean): Resource<Set<String>>
}