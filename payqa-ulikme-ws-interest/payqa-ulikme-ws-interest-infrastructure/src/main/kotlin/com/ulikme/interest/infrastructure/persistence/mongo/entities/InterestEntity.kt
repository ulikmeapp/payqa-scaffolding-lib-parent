package com.ulikme.interest.infrastructure.persistence.mongo.entities

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "interestV2")
open class InterestEntity(
    open var id: String? = null,
    open var name: String = "",
    open var description: String = "",
    open var showInOnBoarding: Boolean = false
)