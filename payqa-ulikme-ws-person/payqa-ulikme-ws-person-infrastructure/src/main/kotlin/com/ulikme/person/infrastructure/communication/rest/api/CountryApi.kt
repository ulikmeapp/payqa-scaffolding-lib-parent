package com.ulikme.person.infrastructure.communication.rest.api

import com.ulikme.country.domain.models.CountryModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

const val COUNTRY_BASE = "api/v1/country"
const val COUNTRY_FIND_BY_CODE = "${COUNTRY_BASE}/{code}"

interface CountryApi {

    @GET(COUNTRY_FIND_BY_CODE)
    fun findByCode(
        @Header("Authorization") token: String,
        @Path("code") code: String
    ): Observable<Response<CountryModel>>

}