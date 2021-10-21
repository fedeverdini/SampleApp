package com.example.sampleapp.model

import com.example.sampleapp.BuildConfig
import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.utils.errors.ErrorCode
import com.example.sampleapp.utils.errors.ErrorUtils
import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber

data class Resource<out T>(val status: Status, val data: T? = null, val error: BaseError? = null) {
    enum class Status {
        //LOADING,
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data = data)
        }

        fun <T> error(error: BaseError): Resource<T> {
            return Resource(Status.ERROR, error = error)
        }

        /*fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }*/

        fun <T> create(response: Response<T>): Resource<T> {
            return if (response.isSuccessful) {
                val obj: T? = response.body()
                success(obj)
            } else {
                val error = ErrorUtils.getErrorFromHttpCode(response.code(), response.message())
                error(error)
            }
        }

        fun <T> create(throwable: Throwable): Resource<T> {
            if (BuildConfig.DEBUG) Timber.d(throwable)
            return error(ErrorUtils.getErrorFromThrowable(throwable))
        }
    }
}