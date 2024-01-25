package com.ulikme.interaction.infrastructure.persistence.mongo.entities

import com.ulikme.person.infrastructure.persistence.mongo.entities.PersonEntity
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "blocking")
open class BlockingEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var blockedPerson: PersonEntity? = null
) : AuditedEntity(), Serializable