package com.ulikme.chat.infrastructure.communication.rest.api

import org.springframework.http.HttpHeaders
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

const val GROUP_MEET_BASE = "api/v1/meet/group"
const val GROUP_MEET_CONFIRM = "$GROUP_MEET_BASE/confirm/{chat-id}"

interface GroupMeetApi {

    @POST(GROUP_MEET_CONFIRM)
    fun confirm(
        @Header(HttpHeaders.AUTHORIZATION) authorization: String,
        @Path("chat-id") chatId: String
    ): Observable<Response<Void>>

}