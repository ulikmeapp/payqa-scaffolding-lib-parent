package com.ulikme.chat.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ulikme.chat.domain.enums.MessageType
import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.person.domain.common.ShortPerson
import dev.payqa.scaffolding.apicrud.design.models.Model

val EMPTY_MESSAGE = MessageModel(
    type = MessageType.WITHOUT_MESSAGES.key,
    content = "Start to chat",
    seen = true
)

data class MessageModel(
    val id: String = "",
    @JsonIgnore
    val chatId: String = "",
    val participant: ShortPerson = ShortPerson(),
    val type: String? = null,
    val content: String = "",
    val seen: Boolean = false,
    val meetInMessage: MeetInMessageModel? = null,
    val edited: Boolean = false
) : Model()

data class MeetInMessageModel(
    val establishment: EstablishmentModel = EstablishmentModel(),
    val date: String = "",
    val hour: String = "",
    val proposeId: String? = null,
    val answerIds: List<String> = emptyList()
) : Model()
