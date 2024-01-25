package com.ulikme.meet.api.facade

import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest

interface MeetFacade {

    fun register(request: MeetRequest): MeetModel

    fun qualify(id: String, qualification: String): MeetModel

}