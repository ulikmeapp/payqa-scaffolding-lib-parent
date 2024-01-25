package com.ulikme.meet.infrastructure.communication.rest.api

import com.ulikme.person.domain.models.PersonModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

const val PERSON_BASE = "api/v1/person"
const val PERSON_FIND_BY_ID = "${PERSON_BASE}/{id}"

interface PersonApi {

    @GET(PERSON_FIND_BY_ID)
    fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Observable<Response<PersonModel>>

}