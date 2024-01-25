package dev.payqa.scaffolding.apicrud.communication.retrofit.interceptor

import dev.payqa.scaffolding.utils.security.JwtUtils
import okhttp3.Interceptor
import okhttp3.Response

class PayqaAuthInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "${JwtUtils.NOMENCLATURE} $token")
                .build()
        )
}