package com.ulikme.person.infrastructure.persistence.mongo.entities

import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "interestV2")
open class InterestEntity(
    open var id: String? = null,
    open var name: String = "",
    open var description: String = "",
    open var showInOnBoarding: Boolean = false
) : Serializable