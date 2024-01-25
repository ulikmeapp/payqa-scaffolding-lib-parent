package com.ulikme.chat.api.rest

import com.ulikme.chat.api.facade.ChatFacade
import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.rr.request.ChatRequest
import com.ulikme.chat.service.ChatService
import com.ulikme.interaction.domain.models.InteractionModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chat")
open class ChatController(
    private val chatFacade: ChatFacade,
    private val chatService: ChatService
) {

    @ApiOperation(
        value = "Paginate chats by person who participate in them",
        nickname = "paginate"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Chats retrieved")
    )
    @GetMapping("/user/{id}")
    fun paginate(
        @PathVariable id: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<ChatModel>> =
        ResponseEntity.ok(chatService.paginate(
            PaginateRequest(
                pageNumber,
                pageSize,
                sortColumn = sortField,
                sortDirection = sortOrder,
                params = mapOf(
                    "personId" to id,
                )
            )
        ))

    @ApiOperation(value = "Find chat by id", nickname = "findById")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Chat not found"),
        ApiResponse(code = 200, message = "Chat found")
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<ChatModel> =
        ResponseEntity.ok(chatService.findById(id, true))

    @ApiOperation(value = "Register a new chat", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Chat created")
    )
    @PostMapping
    fun register(@RequestBody request: ChatRequest): ResponseEntity<ChatModel> =
        ResponseEntity.ok(chatFacade.register(request))

    @ApiOperation(value = "Update chat status", nickname = "updateStatus")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Chat not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Chat updated")
    )
    @PatchMapping("/{id}/status/{status}")
    fun updateStatus(
        @PathVariable id: String,
        @PathVariable status: String
    ): ResponseEntity<ChatModel> =
        ResponseEntity.ok(chatFacade.updateStatus(id, status))


}