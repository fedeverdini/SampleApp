package com.example.sampleapp.utils.network

interface INetworkUtils {
    fun isNetworkAvailable(): Boolean
    fun isInternetAvailable(): Boolean
}