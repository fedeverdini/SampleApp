package com.example.sampleapp.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.concurrent.futures.ResolvableFuture
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.ui.transaction.view.TransactionDetailsListView
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("RestrictedApi")
class SaveTransactionListWorker(context: Context, params: WorkerParameters) :
    ListenableWorker(context, params), KoinComponent {
    private val productRepo: IProductRepo by inject()
    private val inputData = params.inputData.keyValueMap
    private val future: ResolvableFuture<Result> = ResolvableFuture.create()

    companion object {
        const val TRANSACTION_KEY = "TRANSACTION_KEY"
    }

    override fun startWork(): ListenableFuture<Result> {
        if (inputData.containsKey(TRANSACTION_KEY)) {
            val jsonData = inputData[TRANSACTION_KEY] as String
            val transactionList = Gson().fromJson(jsonData, TransactionDetailsListView::class.java)
            if (transactionList.transactions.isNotEmpty()) {
                transactionList.transactions.forEach {
                    productRepo.saveTransactionDetailInCache(it)
                }
            }
            future.set(Result.success())
        }

        return future
    }
}