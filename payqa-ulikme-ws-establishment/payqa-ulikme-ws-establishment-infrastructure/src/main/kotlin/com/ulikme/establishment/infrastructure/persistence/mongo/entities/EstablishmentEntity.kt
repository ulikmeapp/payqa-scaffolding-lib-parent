package com.ulikme.establishment.infrastructure.persistence.mongo.entities

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import com.ulikme.utils.data.AuditedEntity
import com.ulikme.utils.data.PointLocation
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "establishmentV2")
open class EstablishmentEntity(
    open var id: String? = null,
    open var name: String = "",
    open var country: CountryEntity? = null,
    open var location: PointLocation = PointLocation(),
    open var address: String? = null,
    open var schedule: List<String> = emptyList()
) : AuditedEntity(), Serializable