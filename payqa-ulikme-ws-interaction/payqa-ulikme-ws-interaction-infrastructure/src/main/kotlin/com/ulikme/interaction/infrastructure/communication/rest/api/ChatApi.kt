package com.ulikme.interaction.infrastructure.communication.rest.api

import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.rr.request.ChatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import rx.Observable

const val CHAT_BASE = "api/v1/chat"

interface ChatApi {

    @POST(CHAT_BASE)
    fun register(
        @Header("Authorization") token: String,
        @Body request: ChatRequest
    ): Observable<Response<ChatModel>>

}