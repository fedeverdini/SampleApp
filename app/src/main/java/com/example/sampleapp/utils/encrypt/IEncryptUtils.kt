package com.example.sampleapp.utils.encrypt

interface IEncryptUtils {
    fun getHash(ts: Long): String
}