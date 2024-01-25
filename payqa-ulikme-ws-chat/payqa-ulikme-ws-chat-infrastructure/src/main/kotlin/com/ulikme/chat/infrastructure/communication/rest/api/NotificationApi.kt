package com.ulikme.chat.infrastructure.communication.rest.api

import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

const val NOTIFICATION_BASE = "$PERSON_BASE/{id}/notification"
const val SEND_NOTIFICATION = "$NOTIFICATION_BASE/send"

interface NotificationApi {

    @POST(SEND_NOTIFICATION)
    fun send(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: NotificationRequest
    ): Observable<Response<NotificationModel>>

}