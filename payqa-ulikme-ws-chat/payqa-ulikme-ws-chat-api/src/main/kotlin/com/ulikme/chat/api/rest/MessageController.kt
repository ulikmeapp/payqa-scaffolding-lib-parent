package com.ulikme.chat.api.rest

import com.ulikme.chat.api.facade.MessageFacade
import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MessageRequest
import com.ulikme.chat.service.MessageService
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chat/{id}/messages")
open class MessageController(
    private val messageFacade: MessageFacade,
    private val messageService: MessageService
) {

    @ApiOperation(
        value = "Paginate messages by chat",
        nickname = "paginate"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Messages retrieved")
    )
    @GetMapping
    fun paginate(
        @PathVariable id: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<MessageModel>> =
        ResponseEntity.ok(
            messageService.paginateByChat(
                id, PaginateRequest(
                    page = pageNumber,
                    pageSize = pageSize,
                    sortDirection = sortOrder,
                    sortColumn = sortField
                )
            )
        )

    @ApiOperation(value = "Register a new message", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Message created")
    )
    @PostMapping
    fun register(
        @PathVariable id: String,
        @RequestBody request: MessageRequest
    ): ResponseEntity<MessageModel> = ResponseEntity.ok(messageFacade.register(id, request))

    @ApiOperation(value = "Update message content", nickname = "updateContent")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Message not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Message updated")
    )
    @PatchMapping("/{message-id}/content/{content}")
    fun updateContent(
        @PathVariable id: String,
        @PathVariable("message-id") messageId: String,
        @PathVariable content: String
    ): ResponseEntity<MessageModel> = ResponseEntity.ok(messageService.update(id, messageId, content))

}