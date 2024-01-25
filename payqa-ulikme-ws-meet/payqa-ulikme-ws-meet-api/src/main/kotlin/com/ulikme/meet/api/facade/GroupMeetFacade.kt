package com.ulikme.meet.api.facade

import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import javax.servlet.http.HttpServletRequest

interface GroupMeetFacade {

    fun register(request: MeetRequest, servletRequest: HttpServletRequest): MeetModel

    fun confirm(chatId: String)

}