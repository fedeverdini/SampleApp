package com.example.sampleapp.ui.transaction.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class ProductView(
    @PrimaryKey
    val sku: String
)
