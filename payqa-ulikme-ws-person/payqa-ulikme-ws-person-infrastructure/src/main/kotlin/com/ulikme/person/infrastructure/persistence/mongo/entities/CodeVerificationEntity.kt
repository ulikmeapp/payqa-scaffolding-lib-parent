package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "codeVerification")
open class CodeVerificationEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var type: String = "",
    open var content: String = "",
    open var enabled: Boolean = true
) : AuditedEntity(), Serializable