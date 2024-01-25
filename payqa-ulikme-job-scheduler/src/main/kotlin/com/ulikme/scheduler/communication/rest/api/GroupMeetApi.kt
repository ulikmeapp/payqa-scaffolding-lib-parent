package com.ulikme.scheduler.communication.rest.api

import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

const val GROUP_MEET_BASE = "api/v1/meet/group"

interface GroupMeetApi {

    @POST(GROUP_MEET_BASE)
    fun register(@Body request: MeetRequest): Observable<Response<MeetModel>>

}