package com.ulikme.chat.api.facade

import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MessageRequest

interface MessageFacade {

    fun register(id: String, request: MessageRequest): MessageModel

}