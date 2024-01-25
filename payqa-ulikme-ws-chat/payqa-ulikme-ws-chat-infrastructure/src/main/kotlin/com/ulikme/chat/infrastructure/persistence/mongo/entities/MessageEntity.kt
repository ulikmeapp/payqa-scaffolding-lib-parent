package com.ulikme.chat.infrastructure.persistence.mongo.entities

import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.person.domain.common.ShortPerson
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "messageV2")
open class MessageEntity(
    open var id: String? = null,
    open var chatId: String = "",
    open var participant: ShortPerson = ShortPerson(),
    open var type: String? = null,
    open var content: String = "",
    open var seen: Boolean = false,
    open var meetInMessage: MeetInMessageEntity? = null,
    open var edited: Boolean = false
) : AuditedEntity(), Serializable

open class MeetInMessageEntity(
    open var establishment: EstablishmentEntity = EstablishmentEntity(),
    open var date: String = "",
    open var hour: String = "",
    open var proposeId: String? = null,
    open var answerIds: List<String> = emptyList()
)