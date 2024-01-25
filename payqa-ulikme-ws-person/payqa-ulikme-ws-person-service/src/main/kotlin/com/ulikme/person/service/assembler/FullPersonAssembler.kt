package com.ulikme.person.service.assembler

import com.ulikme.person.domain.models.*

object FullPersonAssembler {

    fun fromPerson(
        person: PersonModel,
        photos: List<PhotoModel>?,
        preferences: PreferencesModel?,
        latestLocation: LocationModel?
    ): FullPersonModel =
        FullPersonModel(
            id = person.id,
            personal = person.personal,
            behavior = person.behavior,
            complementary = person.complementary,
            subscription = person.subscription,
            profilePercentage = person.profilePercentage,
            photos = photos ?: emptyList(),
            preferences = preferences ?: PreferencesModel(),
            latestLocation = latestLocation ?: LocationModel()
        )

}