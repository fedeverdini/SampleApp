package com.example.sampleapp.model.rate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Rates")
data class RateListResponse (
    @PrimaryKey
    var lastUpdate: Long,
    val rates : List<Rate>
)