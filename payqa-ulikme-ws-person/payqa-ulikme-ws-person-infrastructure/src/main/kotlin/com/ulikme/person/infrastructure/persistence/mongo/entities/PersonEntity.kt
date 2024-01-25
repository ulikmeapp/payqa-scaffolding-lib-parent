package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import com.ulikme.utils.data.AuditedEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "personV2")
open class PersonEntity(
    @Id
    open var id: String? = null,

    // Personal
    open var name: String = "",
    open var lastName: String? = null,
    open var mobilePhone: String? = null,
    open var email: String = "",
    open var bornDate: String? = null,
    open var age: Int? = null,
    open var country: CountryEntity? = null,

    // Behavior
    open var interests: List<InterestEntity> = emptyList(),
    open var gender: String? = null,
    open var orientations: List<String> = emptyList(),
    open var mobilePhoneVerified: Boolean = false,
    open var emailVerified: Boolean = false,
    open var profileVerified: Boolean = false,

    // Complementary
    open var shortDescription: String? = null,
    open var workPlace: String? = null,
    open var whereLive: String? = null,
    open var professionalRole: String? = null,
    open var picture: String? = null,

    // Subscription
    open var subscribed: Boolean = false,
    open var promoted: Boolean = false,
    open var promotedDate: String? = null,
    open var promotedTimeZone: String? = null,

    open var profilePercentage: Int = 0
) : AuditedEntity(), Serializable