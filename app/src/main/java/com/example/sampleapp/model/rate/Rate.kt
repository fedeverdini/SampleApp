package com.example.sampleapp.model.rate

import com.google.gson.annotations.SerializedName

data class Rate (
    @SerializedName("from") val from : String,
    @SerializedName("to") val to : String,
    @SerializedName("rate") val rate : String
)