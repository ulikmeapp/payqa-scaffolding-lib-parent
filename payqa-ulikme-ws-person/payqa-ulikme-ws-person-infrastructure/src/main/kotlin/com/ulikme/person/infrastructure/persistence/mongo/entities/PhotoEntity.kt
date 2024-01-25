package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "photoV2")
open class PhotoEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var key: String? = null,
    open var url: String? = null,
    open var filename: String? = null,
    open var main: Boolean = false
) : AuditedEntity(), Serializable