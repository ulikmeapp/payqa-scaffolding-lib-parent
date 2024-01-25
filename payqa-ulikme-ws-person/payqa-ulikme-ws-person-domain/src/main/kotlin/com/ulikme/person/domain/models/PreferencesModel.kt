package com.ulikme.person.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.payqa.scaffolding.apicrud.design.models.Model

data class PreferencesModel(
    @JsonIgnore
    val personId: String = "",
    val pushNotifications: Boolean = false,
    val alwaysLocation: Boolean = false,
    val facialRecognition: Boolean = false,
    val groupDating: Boolean = false,
    val showSexualOrientation: Boolean = false,
    val global: Boolean = true,
    val maxAge: Int? = 70,
    val minAge: Int? = 18,
    val maxDistance: Int? = 100,
    val confirmSeen: Boolean = false,
    val showMe: String? = "A",
    val ulikmeNews: Boolean = false,
    val showProfessionalRole: Boolean = false,
    val showGender: Boolean = false,
    val recentActivity: Boolean = false,
    val showOnlyInRange: Boolean = false,
    val showOnlyUntilAge: Boolean = false,
    val showMeInUlikme: Boolean = false,
    val firebaseTokens: List<String> = emptyList()
) : Model()