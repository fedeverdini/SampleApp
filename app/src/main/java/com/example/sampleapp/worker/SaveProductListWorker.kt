package com.example.sampleapp.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.concurrent.futures.ResolvableFuture
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.sampleapp.datasource.IProductRepo
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("RestrictedApi")
class SaveProductListWorker(context: Context, params: WorkerParameters): ListenableWorker(context, params), KoinComponent {

    private val productRepo: IProductRepo by inject()
    private val inputData = params.inputData.keyValueMap
    private val future : ResolvableFuture<Result> = ResolvableFuture.create()

    private val ioScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val PRODUCT_LIST_KEY = "PRODUCT_LIST_KEY"
    }

    override fun startWork(): ListenableFuture<Result> {
        ioScope.launch {
            if (inputData.containsKey(PRODUCT_LIST_KEY)) {
                var products = (inputData[PRODUCT_LIST_KEY] as Array<String>).toList()
                productRepo.clearTransactionDetails()
                productRepo.saveProductListInCache(products)
            }
            future.set(Result.success())
        }

        return future
    }
}