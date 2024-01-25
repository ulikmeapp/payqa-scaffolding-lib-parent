package com.ulikme.scheduler.communication.rest.client

import com.ulikme.scheduler.communication.rest.api.Auth0Api
import com.ulikme.scheduler.communication.rest.auth0.request.TokenRequest
import com.ulikme.scheduler.communication.rest.auth0.response.TokenResponse
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class Auth0RetrofitClient(baseUrl: String) : RetrofitClient<Auth0Api>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(Auth0Api::class.java)
    }

    fun generateToken(request: TokenRequest): TokenResponse {
        log.info("[generateToken] init. [request=$request]")

        var output: TokenResponse? = null

        super.manager.generateToken(request).subscribe(
            RxObserver(object : RetrofitCallback<TokenResponse> {
                override fun onSuccess(response: TokenResponse) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[generateToken] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to generate auth0 token.")
    }

}