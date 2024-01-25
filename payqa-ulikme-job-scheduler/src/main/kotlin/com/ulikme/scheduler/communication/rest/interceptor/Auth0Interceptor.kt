package com.ulikme.scheduler.communication.rest.interceptor

import com.ulikme.utils.http.support.BackendContext
import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.http.HttpHeaders

open class Auth0Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${BackendContext.getToken()}")
                .build()
        )

}