package com.ulikme.interaction.infrastructure.persistence.mongo.entities

import com.ulikme.person.infrastructure.persistence.mongo.entities.PersonEntity
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "interactionV2")
open class InteractionEntity(
    open var id: String? = null,
    open var type: String = "",
    open var classification: String? = null,
    open var zone: String? = null,
    open var personInteracted: PersonEntity? = null,
    open var match: Boolean = false,
    open var inGroup: Boolean = false
) : AuditedEntity(), Serializable