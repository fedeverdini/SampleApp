package com.example.sampleapp.utils.strings

interface IStringUtils {
    fun createFullImageUrl(url: String?, ext: String?, https: Boolean = false): String
}