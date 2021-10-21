package com.example.sampleapp.utils.date

import java.util.*

interface IDateUtils {
    fun isCacheExpired(lastUpdate: Long?): Boolean
    fun Date.extract(millis: Long): Long
}