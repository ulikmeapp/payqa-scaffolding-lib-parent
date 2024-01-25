package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "photoVerification")
open class PhotoVerificationEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var key: String? = null,
    open var url: String? = null,
    open var filename: String? = null
) : AuditedEntity(), Serializable