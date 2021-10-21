package com.example.sampleapp.db.converters

import androidx.room.TypeConverter
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse
import com.google.gson.Gson
import java.util.*

class RatesConverter {
    @TypeConverter
    fun fromRateListToJson(value: RateListResponse?): String? {
        return Gson().toJson(value, RateListResponse::class.java)
    }

    @TypeConverter
    fun fromJsonToRateList(json: String?): RateListResponse {
        return Gson().fromJson(json, RateListResponse::class.java)
    }

    @TypeConverter
    fun fromListToJson(value: List<Rate>?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToList(value: String) = Gson().fromJson(value, Array<Rate>::class.java).toList()

    @TypeConverter
    fun fromRateToJson(value: Rate): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToRate(value: String) = Gson().fromJson(value, Rate::class.java)
}