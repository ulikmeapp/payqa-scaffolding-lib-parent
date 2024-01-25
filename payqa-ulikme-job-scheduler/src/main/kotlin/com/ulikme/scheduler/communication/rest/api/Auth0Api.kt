package com.ulikme.scheduler.communication.rest.api

import com.ulikme.scheduler.communication.rest.auth0.request.TokenRequest
import com.ulikme.scheduler.communication.rest.auth0.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

const val OAUTH_TOKEN = "oauth/token"

interface Auth0Api {

    @POST(OAUTH_TOKEN)
    fun generateToken(@Body request: TokenRequest): Observable<Response<TokenResponse>>

}