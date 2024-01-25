package com.ulikme.chat.service

import com.ulikme.chat.domain.models.MessageModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface MessageService {

    fun paginateByChat(chatId: String, request: PaginateRequest<MessageModel>): Page<MessageModel>

    fun findLatestByChat(chatId: String): MessageModel?

    fun findById(id: String): MessageModel

    fun findByIdOrNull(id: String): MessageModel?

    fun findByChatAndId(chatId: String, id: String): MessageModel

    fun register(message: MessageModel): MessageModel

    fun update(chatId: String, id: String, content: String): MessageModel

    fun answerPropose(chatId: String, id: String, proposeMessageId: String): MessageModel

}