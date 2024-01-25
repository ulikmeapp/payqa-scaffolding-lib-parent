package com.ulikme.chat.infrastructure.communication.rest.api

import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import org.springframework.http.HttpHeaders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import rx.Observable

const val MEET_BASE = "api/v1/meet"

interface MeetApi {

    @POST(MEET_BASE)
    fun register(
        @Header(HttpHeaders.AUTHORIZATION) authorization: String,
        @Body request: MeetRequest
    ): Observable<Response<MeetModel>>

}