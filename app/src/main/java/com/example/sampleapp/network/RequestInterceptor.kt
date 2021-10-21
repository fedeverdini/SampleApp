package com.example.sampleapp.network

import com.example.sampleapp.model.error.NoInternetException
import com.example.sampleapp.model.error.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.example.sampleapp.utils.constants.HttpHeaders.ACCEPT_HEADER
import com.example.sampleapp.utils.constants.HttpHeaders.ACCEPT_HEADER_JSON
import com.example.sampleapp.utils.network.INetworkUtils
import timber.log.Timber

class RequestInterceptor : Interceptor, KoinComponent {

    private val networkUtils: INetworkUtils by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkUtils.isNetworkAvailable()) throw NoNetworkException()
        if (!networkUtils.isInternetAvailable()) throw NoInternetException()

        val original = chain.request()

        val request = original.newBuilder()
            .header(ACCEPT_HEADER, ACCEPT_HEADER_JSON)
            .method(original.method(), original.body())
            .build()

        Timber.d("Request: ${request.method()} ${request.url()}")
        Timber.d("Headers: ${request.headers()}")
        return chain.proceed(request)
    }
}