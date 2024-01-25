package com.ulikme.chat.service

import com.ulikme.chat.domain.models.ChatModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface ChatService {

    fun paginate(request: PaginateRequest<ChatModel>): Page<ChatModel>

    fun findById(id: String, withLatestMessages: Boolean = false): ChatModel

    fun save(chat: ChatModel): ChatModel

}