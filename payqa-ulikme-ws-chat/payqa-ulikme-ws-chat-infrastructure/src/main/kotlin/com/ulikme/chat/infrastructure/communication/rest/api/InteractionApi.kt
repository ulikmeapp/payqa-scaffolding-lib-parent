package com.ulikme.chat.infrastructure.communication.rest.api

import com.ulikme.interaction.domain.models.InteractionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

const val INTERACTION_BASE = "api/v1/interaction"
const val INTERACTION_FIND_BY_ID = "${INTERACTION_BASE}/{id}"

interface InteractionApi {

    @GET(INTERACTION_FIND_BY_ID)
    fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Observable<Response<InteractionModel>>

}