package com.ulikme.meet.api.rest

import com.ulikme.meet.api.facade.GroupMeetFacade
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/meet/group")
open class GroupMeetController(
    private val groupMeetFacade: GroupMeetFacade
) {

    @PostMapping
    fun register(
        @RequestBody request: MeetRequest,
        servletRequest: HttpServletRequest
    ): ResponseEntity<MeetModel> =
        ResponseEntity.ok(groupMeetFacade.register(request, servletRequest))

    @PostMapping("/confirm/{chat-id}")
    fun confirm(@PathVariable("chat-id") chatId: String): ResponseEntity<Void> {
        groupMeetFacade.confirm(chatId)
        return ResponseEntity.accepted().build()
    }

}