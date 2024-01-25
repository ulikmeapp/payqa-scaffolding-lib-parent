package com.ulikme.chat.infrastructure.persistence.mongo.entities

import com.ulikme.interaction.infrastructure.persistence.mongo.entities.InteractionEntity
import com.ulikme.person.domain.common.ShortPerson
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "chatV2")
open class ChatEntity(
    @Id
    open var id: String? = null,
    open var participants: List<ShortPerson>? = null,
    open var interaction: InteractionEntity? = null,
    open var status: String? = null,
    open var latestMessage: MessageEntity? = null
) : AuditedEntity(), Serializable
