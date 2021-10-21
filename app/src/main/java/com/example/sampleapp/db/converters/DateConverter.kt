package com.example.sampleapp.db.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromLongtoDate(dateLong: Long?) = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDateToLong(date: Date?) = date?.time
}