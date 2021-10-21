package com.example.sampleapp.utils.amount

import com.example.sampleapp.model.rate.Rate
import java.math.BigDecimal
import java.math.RoundingMode

class AmountUtils : IAmountUtils {
    override fun roundToTwoDecimals(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    override fun getRate(
        checkedCurrencies: List<String>,
        originalCurrency: String,
        finalCurrency: String,
        rates: List<Rate>
    ): Double? {

        if (originalCurrency == finalCurrency) return 1.0

        val ratesFrom = rates.filter { r -> r.from == originalCurrency }
        if (ratesFrom.any { r -> r.to == finalCurrency }) {
            return ratesFrom.first { r -> r.to == finalCurrency }.rate.toDouble()
        } else {
            val newCheckedCurrencies = checkedCurrencies.toMutableList()
            ratesFrom.forEach { rate ->
                newCheckedCurrencies.add(rate.from)
                val newRates = rates.toMutableList().apply { removeIf { r -> r.to in newCheckedCurrencies || r.from in newCheckedCurrencies } }
                if (newRates.isEmpty()) return@forEach

                val finalRate = getRate(newCheckedCurrencies, rate.to, finalCurrency, newRates)
                if (finalRate != null) {
                    return rate.rate.toDouble() * finalRate
                } else {
                    return@forEach
                }
            }
            return null
        }
    }
}