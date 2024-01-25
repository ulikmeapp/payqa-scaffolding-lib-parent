package com.ulikme.meet.infrastructure.communication.rest.api

import com.ulikme.establishment.domain.models.EstablishmentModel
import org.springframework.http.HttpHeaders
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

const val ESTABLISHMENT_BASE = "api/v1/establishment"
const val ESTABLISHMENT_FIND_BY_ID = "$ESTABLISHMENT_BASE/{id}"

interface EstablishmentApi {

    @GET(ESTABLISHMENT_FIND_BY_ID)
    fun findById(
        @Header(HttpHeaders.AUTHORIZATION) authorization: String,
        @Path("id") id: String
    ): Observable<Response<EstablishmentModel>>

}