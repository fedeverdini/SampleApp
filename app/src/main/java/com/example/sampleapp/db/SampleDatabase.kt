package com.example.sampleapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sampleapp.db.converters.DateConverter
import com.example.sampleapp.db.converters.RatesConverter
import com.example.sampleapp.db.converters.TransactionsConverter
import com.example.sampleapp.model.rate.RateListResponse
import com.example.sampleapp.model.transaction.TransactionListResponse

@Database(entities = [TransactionListResponse::class, RateListResponse::class], version = 1, exportSchema = false)
@TypeConverters(TransactionsConverter::class, RatesConverter::class, DateConverter::class)
abstract class SampleDatabase : RoomDatabase() {
    abstract fun transactionListDao(): TransactionListDao
    abstract fun rateListDao(): RateListDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, SampleDatabase::class.java, "SampleDatabase")
                .fallbackToDestructiveMigration()
                .build()
    }
}