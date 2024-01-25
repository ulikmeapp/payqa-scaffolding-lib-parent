package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.PreferencesEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object PreferencesModelMapper : ModelMapper<PreferencesModel, PreferencesEntity>() {

    override fun map(input: PreferencesModel): PreferencesEntity =
        PreferencesEntity(
            personId = input.personId,
            pushNotifications = input.pushNotifications,
            alwaysLocation = input.alwaysLocation,
            facialRecognition = input.facialRecognition,
            groupDating = input.groupDating,
            showSexualOrientation = input.showSexualOrientation,
            global = input.global,
            minAge = input.minAge,
            maxAge = input.maxAge,
            maxDistance = input.maxDistance,
            confirmSeen = input.confirmSeen,
            showMe = input.showMe,
            ulikmeNews = input.ulikmeNews,
            showProfessionalRole = input.showProfessionalRole,
            showGender = input.showGender,
            recentActivity = input.recentActivity,
            showOnlyInRange = input.showOnlyInRange,
            showOnlyUntilAge = input.showOnlyUntilAge,
            showMeInUlikme = input.showMeInUlikme,
            firebaseTokens = input.firebaseTokens
        )

    override fun inverseMap(input: PreferencesEntity): PreferencesModel =
        PreferencesModel(
            pushNotifications = input.pushNotifications,
            alwaysLocation = input.alwaysLocation,
            facialRecognition = input.facialRecognition,
            groupDating = input.groupDating,
            showSexualOrientation = input.showSexualOrientation,
            global = input.global,
            minAge = input.minAge,
            maxAge = input.maxAge,
            maxDistance = input.maxDistance,
            confirmSeen = input.confirmSeen,
            showMe = input.showMe,
            ulikmeNews = input.ulikmeNews,
            showProfessionalRole = input.showProfessionalRole,
            showGender = input.showGender,
            recentActivity = input.recentActivity,
            showOnlyInRange = input.showOnlyInRange,
            showOnlyUntilAge = input.showOnlyUntilAge,
            showMeInUlikme = input.showMeInUlikme,
            firebaseTokens = input.firebaseTokens
        )

}