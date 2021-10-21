package com.example.sampleapp.db.converters

import androidx.room.TypeConverter
import com.example.sampleapp.model.transaction.Transaction
import com.example.sampleapp.model.transaction.TransactionListResponse
import com.google.gson.Gson
import java.util.*

class TransactionsConverter {
    @TypeConverter
    fun fromTransactionListToJson(value: TransactionListResponse?): String? {
        return Gson().toJson(value, TransactionListResponse::class.java)
    }

    @TypeConverter
    fun fromJsonToTransactionList(json: String?): TransactionListResponse {
        return Gson().fromJson(json, TransactionListResponse::class.java)
    }

    @TypeConverter
    fun fromListToJson(value: List<Transaction>?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToList(value: String) = Gson().fromJson(value, Array<Transaction>::class.java).toList()

    @TypeConverter
    fun fromTransactionToJson(value: Transaction): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToTransaction(value: String) = Gson().fromJson(value, Transaction::class.java)
}