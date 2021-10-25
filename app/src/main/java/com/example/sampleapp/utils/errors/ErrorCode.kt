package com.example.sampleapp.utils.errors

enum class ErrorCode(val message: String) {
    CONNECTION_TIMEOUT("Your request it taking to long, we'll send a superhero to check what's happening."),
    NOT_IMPLEMENTED(""),
    NO_RATES_AVAILABLE("We couldn't get all the information needed to continue, please try again later"),
    NO_INTERNET("No internet available, please check your connected WiFi or Data"),
    IO_EXCEPTION("No network available, please check your WiFi or Data connection"),
    UNKNOWN("Something went wrong, please try in a few minutes"),
    HTTP400(""),
    HTTP404("We couldn't find what you're looking for"),
    HTTP409(""),
    HTTP500("We're having some problems reaching our servers, please try again in a few minutes")
}