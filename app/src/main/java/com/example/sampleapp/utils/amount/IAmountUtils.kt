package com.example.sampleapp.utils.amount

import com.example.sampleapp.model.rate.Rate

interface IAmountUtils {
    fun getRate(
        checkedCurrencies: List<String>,
        originalCurrency: String,
        finalCurrency: String,
        rates: List<Rate>
    ): Double?

    fun roundToTwoDecimals(value: Double): Double
}