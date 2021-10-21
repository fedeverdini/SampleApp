package com.example.sampleapp.usecase

import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.enum.Currency
import com.example.sampleapp.model.Resource
import com.example.sampleapp.model.Resource.Status.SUCCESS
import com.example.sampleapp.model.Resource.Status.ERROR
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.ui.transaction.view.TransactionDetailsListView
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.amount.IAmountUtils
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils.Companion.createError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTransactionDetailsUseCase(private val productRepo: IProductRepo) :
    IGetTransactionDetailsUseCase,
    KoinComponent {

    private val amountUtils: IAmountUtils by inject()

    private var conversionRateError = false

    override suspend fun getTransactionDetails(
        sku: String,
        finalCurrency: Currency,
        rates: List<Rate>
    ): Resource<TransactionDetailsListView> {
        val cacheData = productRepo.getTransactionListFromCache()
        val txList =
            if (cacheData.data == null) {
                productRepo.getTransactionListFromApi()
            } else {
                cacheData
            }
        return when (txList.status) {
            SUCCESS -> {
                return txList.data?.let { transactionList ->
                    if (transactionList.isEmpty()) {
                        Resource.success(
                            TransactionDetailsListView(
                                total = 0.0,
                                currency = finalCurrency,
                                transactions = emptyList()
                            )
                        )
                    } else {
                        val filteredTransactions =
                            filterTransactions(sku, rates, finalCurrency, transactionList)

                        val total =
                            filteredTransactions.sumByDouble { transaction -> transaction.finalAmount }

                        val response = TransactionDetailsListView(
                            total = total,
                            currency = finalCurrency,
                            transactions = filteredTransactions,
                            conversionRateError = conversionRateError
                        )
                        Resource.success(response)
                    }
                } ?: run {
                    Resource.success(
                        TransactionDetailsListView(
                            total = 0.0,
                            currency = finalCurrency,
                            transactions = emptyList()
                        )
                    )
                }
            }
            ERROR -> {
                Resource.error(txList.error ?: createError(ErrorCode.UNKNOWN))
            }
        }
    }

    private fun filterTransactions(
        sku: String,
        rates: List<Rate>,
        finalCurrency: Currency,
        transactionList: List<Transaction>
    ): List<TransactionDetailsView> {
        var mutableRates: MutableList<Rate> = rates.toMutableList()
        return transactionList.filter { tx -> tx.sku == sku }.mapNotNull { tx ->
            val rate = amountUtils.getRate(emptyList(), tx.currency, finalCurrency.name, mutableRates)
            rate?.let { r ->
                mutableRates = checkUpdateRate(tx.currency, finalCurrency.name, rate, mutableRates)
                val finalAmount = amountUtils.roundToTwoDecimals(r * tx.amount.toDouble())
                val originalAmount = amountUtils.roundToTwoDecimals(tx.amount.toDouble())
                TransactionDetailsView(
                    sku = tx.sku,
                    originalCurrency = tx.currency,
                    originalAmount = originalAmount,
                    finalCurrency = finalCurrency.name,
                    finalAmount = finalAmount
                )
            } ?: run {
                conversionRateError = true
                return@run null
            }
        }
    }

    private fun checkUpdateRate(from: String, to: String, rate: Double, rates: MutableList<Rate>): MutableList<Rate> {
        return if (!rates.any { r -> r.from == from && r.to == to }) {
            val newRates = rates.apply { add(Rate(from, to, rate.toString())) }
            productRepo.saveRateListInCache(newRates)
            return newRates
        } else {
            rates
        }
    }
}