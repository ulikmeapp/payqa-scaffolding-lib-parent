package com.ulikme.person.domain.rr.request

data class PreferencesRequest(
    val pushNotifications: Boolean? = null,
    val alwaysLocation: Boolean? = null,
    val facialRecognition: Boolean? = null,
    val groupDating: Boolean? = null,
    val showSexualOrientation: Boolean? = null,
    val global: Boolean? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val maxDistance: Int? = null,
    val confirmSeen: Boolean? = null,
    val showMe: String? = null,
    val ulikmeNews: Boolean? = null,
    val showProfessionalRole: Boolean? = null,
    val showGender: Boolean? = null,
    val recentActivity: Boolean? = null,
    val showOnlyInRange: Boolean? = null,
    val showOnlyUntilAge: Boolean? = null,
    val showMeInUlikme: Boolean? = null,
    val firebaseToken: String? = null
)
