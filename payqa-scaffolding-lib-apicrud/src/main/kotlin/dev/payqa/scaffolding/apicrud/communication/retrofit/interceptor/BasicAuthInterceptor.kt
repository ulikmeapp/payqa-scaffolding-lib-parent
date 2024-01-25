package dev.payqa.scaffolding.apicrud.communication.retrofit.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

open class BasicAuthInterceptor(
    username: String,
    password: String
) : Interceptor {

    private val credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", this.credentials)
                .build()
        )

}