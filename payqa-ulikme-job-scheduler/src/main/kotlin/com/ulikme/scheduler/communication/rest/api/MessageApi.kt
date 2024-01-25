package com.ulikme.scheduler.communication.rest.api

import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

const val MESSAGE_BASE = "api/v1/chat/{id}/messages"

interface MessageApi {

    @POST(MESSAGE_BASE)
    fun register(
        @Path("id") chatId: String,
        @Body message: MessageRequest
    ): Observable<Response<MessageModel>>

}