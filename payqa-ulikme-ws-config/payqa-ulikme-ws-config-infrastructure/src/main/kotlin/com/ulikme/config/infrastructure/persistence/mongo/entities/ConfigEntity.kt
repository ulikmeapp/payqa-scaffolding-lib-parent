package com.ulikme.config.infrastructure.persistence.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "config")
open class ConfigEntity(

    @Id
    open var id: String? = null,
    open var appProperties: Map<String, Any> = emptyMap()

)