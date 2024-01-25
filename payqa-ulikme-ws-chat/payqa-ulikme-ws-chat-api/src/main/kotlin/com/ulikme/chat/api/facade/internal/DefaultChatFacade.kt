package com.ulikme.chat.api.facade.internal

import com.ulikme.chat.api.facade.ChatFacade
import com.ulikme.chat.domain.enums.ChatStatus
import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.rr.request.ChatRequest
import com.ulikme.chat.infrastructure.communication.rest.client.InteractionRetrofitClient
import com.ulikme.chat.infrastructure.communication.rest.client.PersonRetrofitClient
import com.ulikme.chat.service.ChatService
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.data.utils.Validator
import org.springframework.stereotype.Service

@Service
open class DefaultChatFacade(
    private val chatService: ChatService,
    private val personRetrofitClient: PersonRetrofitClient,
    private val interactionRetrofitClient: InteractionRetrofitClient
) : ChatFacade {

    override fun register(request: ChatRequest): ChatModel =
        chatService.save(with(request) {
            Validator.checkNotNull(participants, ChatRequest::participants.name)
            Validator.checkFalse(participants!!.isEmpty(), "Must there are 1 participant at least")
            ChatModel(
                participants = participants!!
                    .toMutableList()
                    .apply { add(SecurityContext.getUser().id!!) }
                    .distinct()
                    .map { personRetrofitClient.findById(it) }
                    .map { it.asShort() },
                interaction = interactionId?.let { interactionRetrofitClient.findById(it) },
                status = ChatStatus.OPENED.key
            )
        })

    override fun updateStatus(id: String, status: String): ChatModel =
        chatService.findById(id).let { chat ->
            Validator.checkTrue(
                ChatStatus.values().any { statusEnum -> statusEnum.key == status },
                "Invalid [${ChatModel::status.name}]. " +
                        "It must be between the following values: ${ChatStatus.values()}",
                "invalidStatus"
            )
            chatService.save(chat.copy(status = status))
        }

}