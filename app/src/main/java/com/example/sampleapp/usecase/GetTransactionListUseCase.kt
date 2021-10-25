package com.example.sampleapp.usecase

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.enum.Currency
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.ui.transaction.view.ProductView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.amount.IAmountUtils
import com.example.sampleapp.worker.SaveProductListWorker

@SuppressLint("RestrictedApi")
class GetTransactionListUseCase(
    private val context: Context,
    private val productRepo: IProductRepo,
    private val amountUtils: IAmountUtils
) :
    IGetTransactionListUseCase {

    private val workManager: WorkManager = WorkManager.getInstance(context);

    override suspend fun generateTransactionList(rates: List<Rate>, finalCurrency: Currency) {
        val txnData = productRepo.getTransactionsFromApi()
        when (txnData.status) {
            SUCCESS -> {
                if (txnData.data.isNullOrEmpty()) {
                    return
                } else {
                    val txnList = txnData.data
                    val productList = txnList.map { txn -> txn.sku }.toSortedSet()
                    if (productList.isNotEmpty()) {
                        saveProductListInCache(productList)

                        /**
                         * TODO: IMPORTANT!
                         * Make this task in background
                         * check how to solve the issue of max data as input data for worker
                         */
                        productRepo.clearTransactionDetails()
                        txnList.saveTransactionDetailsList(rates, finalCurrency)
                    }
                }
            }
            ERROR -> Unit
        }
    }

    private fun saveProductListInCache(productList: Set<String>) {
        val data = Data.Builder()
            .put(SaveProductListWorker.PRODUCT_LIST_KEY, productList.toTypedArray())
            .build()
        val request = OneTimeWorkRequest.Builder(SaveProductListWorker::class.java)
            .setInputData(data)
            .build()
        workManager.enqueueUniqueWork(
            SaveProductListWorker::class.java.simpleName,
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    private fun List<Transaction>.saveTransactionDetailsList(
        rates: List<Rate>,
        finalCurrency: Currency
    ) {
        var mutableRates: MutableList<Rate> = rates.toMutableList()
        this@saveTransactionDetailsList.forEach { txn ->
            val rate =
                amountUtils.getRate(emptyList(), txn.currency, finalCurrency.name, mutableRates)
            rate?.let { r ->
                mutableRates = checkUpdateRate(txn.currency, finalCurrency.name, rate, mutableRates)
                val finalAmount = amountUtils.roundToTwoDecimals(r * txn.amount.toDouble())
                val originalAmount = amountUtils.roundToTwoDecimals(txn.amount.toDouble())
                val txnDetail = TransactionDetailsView(
                    sku = txn.sku,
                    originalCurrency = txn.currency,
                    originalAmount = originalAmount,
                    finalCurrency = finalCurrency.name,
                    finalAmount = finalAmount
                )
                productRepo.saveTransactionDetailInCache(txnDetail)
            } ?: run {
                // TODO("Notify error for the specific transaction")
                //conversionRateError = true
            }
        }
    }

    private fun checkUpdateRate(
        from: String,
        to: String,
        rate: Double,
        rates: MutableList<Rate>
    ): MutableList<Rate> {
        return if (!rates.any { r -> r.from == from && r.to == to }) {
            val roundedRate = amountUtils.roundToTwoDecimals(rate)
            val newRates = rates.apply { add(Rate(from, to, roundedRate.toString())) }
            productRepo.saveRateListInCache(newRates)
            return newRates
        } else {
            rates
        }
    }
}