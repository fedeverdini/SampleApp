package com.example.sampleapp.utils.extension

object DoubleExtension {
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}