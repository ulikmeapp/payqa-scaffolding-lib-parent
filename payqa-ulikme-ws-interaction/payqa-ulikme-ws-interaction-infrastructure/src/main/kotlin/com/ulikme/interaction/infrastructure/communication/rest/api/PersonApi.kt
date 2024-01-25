package com.ulikme.interaction.infrastructure.communication.rest.api

import com.ulikme.person.domain.models.FullPersonModel
import com.ulikme.person.domain.models.PersonModel
import dev.payqa.scaffolding.apicrud.design.models.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import java.math.BigDecimal

const val PERSON_BASE = "api/v1/person"
const val PERSON_FIND_BY_ID = "$PERSON_BASE/{id}"
const val PERSON_FIND_BY_ID_FULL = "$PERSON_BASE/{id}/full"
const val PERSON_LIST_ONLY = "$PERSON_BASE/only"

interface PersonApi {

    @GET(PERSON_FIND_BY_ID)
    fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Observable<Response<PersonModel>>

    @GET(PERSON_FIND_BY_ID_FULL)
    fun findByIdFull(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Observable<Response<FullPersonModel>>

    @GET(PERSON_LIST_ONLY)
    fun listOnly(
        @Header("Authorization") token: String,
        @Query("lat") latitude: BigDecimal?,
        @Query("lng") longitude: BigDecimal?,
        @Query("dst") distance: Int?,
        @Query("sm") showMe: String?,
        @Query("age") age: Int?,
        @Query("id") vararg id: String
    ): Observable<Response<Data<FullPersonModel>>>

}
