package com.example.sampleapp.utils.preferences

interface IPreferenceUtils {
    fun remove(key: String)
    fun putLong(key: String, value: Long)
    fun getLong(key: String): Long
    fun putString(key: String, value: String)
    fun getString(key: String): String?
}