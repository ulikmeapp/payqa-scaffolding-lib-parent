package com.ulikme.chat.api.facade

import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.rr.request.ChatRequest

interface ChatFacade {

    fun register(request: ChatRequest): ChatModel

    fun updateStatus(id: String, status: String): ChatModel

}