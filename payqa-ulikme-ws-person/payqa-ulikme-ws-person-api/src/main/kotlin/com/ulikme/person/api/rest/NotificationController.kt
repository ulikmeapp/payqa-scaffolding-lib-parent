package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.NotificationFacade
import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/person/{id}/notification")
open class NotificationController(
    private val notificationFacade: NotificationFacade
) {

    @ApiOperation(value = "Send notification to specific person", nickname = "send")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 409, message = "User without assigned token"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Notification send")
    )
    @PostMapping("/send")
    fun send(
        @PathVariable id: String,
        @RequestBody request: NotificationRequest
    ): ResponseEntity<NotificationModel> =
        ResponseEntity.ok(notificationFacade.send(id, request))

}