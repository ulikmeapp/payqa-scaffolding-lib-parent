package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.domain.rr.request.PreferencesRequest

object PreferencesRequestAssembler {

    fun toModel(request: PreferencesRequest, currentPreferences: PreferencesModel): PreferencesModel =
        PreferencesModel(
            pushNotifications = request.pushNotifications ?: currentPreferences.pushNotifications,
            alwaysLocation = request.alwaysLocation ?: currentPreferences.alwaysLocation,
            facialRecognition = request.facialRecognition ?: currentPreferences.facialRecognition,
            groupDating = request.groupDating ?: currentPreferences.groupDating,
            showSexualOrientation = request.showSexualOrientation ?: currentPreferences.showSexualOrientation,
            global = request.global ?: currentPreferences.global,
            minAge=request.minAge ?:currentPreferences.minAge,
            maxAge = request.maxAge ?: currentPreferences.maxAge,
            maxDistance = request.maxDistance ?: currentPreferences.maxDistance,
            confirmSeen = request.confirmSeen ?: currentPreferences.confirmSeen,
            showMe = request.showMe ?: currentPreferences.showMe,
            ulikmeNews = request.ulikmeNews ?: currentPreferences.ulikmeNews,
            showProfessionalRole = request.showProfessionalRole ?: currentPreferences.showProfessionalRole,
            showGender = request.showGender ?: currentPreferences.showGender,
            recentActivity = request.recentActivity ?: currentPreferences.recentActivity,
            showOnlyInRange = request.showOnlyInRange ?: currentPreferences.showOnlyInRange,
            showOnlyUntilAge = request.showOnlyUntilAge ?: currentPreferences.showOnlyUntilAge,
            showMeInUlikme = request.showMeInUlikme ?: currentPreferences.showMeInUlikme,
            firebaseTokens = request.firebaseToken?.let { firebaseToken ->
                if (currentPreferences.firebaseTokens.none { it == firebaseToken }) {
                    mutableListOf<String>().apply {
                        addAll(currentPreferences.firebaseTokens)
                        add(firebaseToken)
                    }
                } else currentPreferences.firebaseTokens
            } ?: currentPreferences.firebaseTokens
        )

}