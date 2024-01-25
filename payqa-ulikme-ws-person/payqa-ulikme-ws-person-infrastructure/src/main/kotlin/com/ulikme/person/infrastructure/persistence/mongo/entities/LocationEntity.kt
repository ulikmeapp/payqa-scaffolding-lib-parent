package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.person.infrastructure.persistence.mongo.enums.LocationType
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.math.BigDecimal

@Document(collection = "location")
open class LocationEntity(
    open var id: String? = null,
    open var personId: String = "",
    open var coordinates: Array<Double> = emptyArray(),
    open var type: String = LocationType.POINT.value
) : AuditedEntity(), Serializable
