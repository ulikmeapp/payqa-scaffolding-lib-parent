package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "notification")
open class NotificationEntity(
    @Id
    open var id: String? = null,
    open var personId: String = "",
    open var type: String = "",
    open var content: String = "",
    open var metadata: Any? = null,
    open var date: String? = null,
    open var send: Boolean = true
) : AuditedEntity(), Serializable