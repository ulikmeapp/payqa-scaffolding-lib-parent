package com.ulikme.country.infrastructure.persistence.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("countryV2")
open class CountryEntity(
    @Id
    open var id: String? = null,
    open var code: String = "",
    open var name: String = "",
    open var phoneCode: String = ""
)