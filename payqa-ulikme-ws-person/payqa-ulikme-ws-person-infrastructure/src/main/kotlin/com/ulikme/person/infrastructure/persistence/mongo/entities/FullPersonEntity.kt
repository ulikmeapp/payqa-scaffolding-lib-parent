package com.ulikme.person.infrastructure.persistence.mongo.entities

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "fullPersonV2")
open class FullPersonEntity(
    id: String? = null,
    name: String = "",
    lastName: String? = null,
    mobilePhone: String? = null,
    email: String = "",
    bornDate: String? = null,
    age: Int? = null,
    country: CountryEntity? = null,
    interests: List<InterestEntity> = emptyList(),
    gender: String? = null,
    orientations: List<String> = emptyList(),
    mobilePhoneVerified: Boolean = false,
    emailVerified: Boolean = false,
    profileVerified: Boolean = false,
    shortDescription: String? = null,
    workPlace: String? = null,
    whereLive: String? = null,
    professionalRole: String? = null,
    picture: String? = null,
    subscribed: Boolean = false,
    promoted: Boolean = false,
    promotedDate: String? = null,
    promotedTimeZone: String? = null,
    profilePercentage: Int = 0,
    open var photos: List<PhotoEntity> = emptyList(),
    open var preferences: PreferencesEntity? = null,
    open var latestLocation: LocationEntity? = null
) : PersonEntity(
    id,
    name,
    lastName,
    mobilePhone,
    email,
    bornDate,
    age,
    country,
    interests,
    gender,
    orientations,
    mobilePhoneVerified,
    emailVerified,
    profileVerified,
    shortDescription,
    workPlace,
    whereLive,
    professionalRole,
    picture,
    subscribed,
    promoted,
    promotedDate,
    promotedTimeZone,
    profilePercentage
), Serializable {

    // Variables used only for query purpose into the main search
    var interactions: List<Any>? = null; private set
    var distance: Double? = null; private set
    var locks: List<Any>? = null; private set

}