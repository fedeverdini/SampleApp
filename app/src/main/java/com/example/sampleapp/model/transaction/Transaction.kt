package com.example.sampleapp.model.transaction

import com.google.gson.annotations.SerializedName

data class Transaction (
    @SerializedName("sku") val sku : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("amountDouble") val amountDouble : Double = amount.toDouble(),
    @SerializedName("currency") val currency : String
)