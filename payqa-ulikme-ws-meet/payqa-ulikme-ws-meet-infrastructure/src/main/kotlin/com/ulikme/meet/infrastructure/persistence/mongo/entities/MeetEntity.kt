package com.ulikme.meet.infrastructure.persistence.mongo.entities

import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.person.domain.common.ShortPerson
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "meetV2")
open class MeetEntity(
    open var id: String? = null,
    open var chatId: String? = null,
    open var date: String? = null,
    open var assistants: List<AssistantEntity> = emptyList(),
    open var establishment: EstablishmentEntity? = null,
    open var group: Boolean = false,
    open var status: String = ""
) : AuditedEntity(), Serializable

open class AssistantEntity(
    open var information: ShortPerson? = null,
    open var qualification: Int? = null,
    open var confirmed: Boolean = true
) : Serializable