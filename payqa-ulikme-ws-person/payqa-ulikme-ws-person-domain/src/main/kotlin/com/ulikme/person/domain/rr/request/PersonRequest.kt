package com.ulikme.person.domain.rr.request

data class PersonRequest(
    val personal: Personal? = null,
    val behavior: Behavior? = null,
    val complementary: Complementary? = null
)

data class Personal(
    val name: String? = null,
    val lastName: String? = null,
    val mobilePhone: String? = null,
    val email: String? = null,
    val bornDate: String? = null,
    val country: String? = null
)

data class Behavior(
    val interests: List<String>? = null,
    val gender: String? = null,
    val orientations: List<String>? = null,
    val mobilePhoneVerified: Boolean? = null,
    val emailVerified: Boolean? = null,
    val profileVerified: Boolean? = null
)

data class Complementary(
    val shortDescription: String? = null,
    val workPlace: String? = null,
    val whereLive: String? = null,
    val professionalRole: String? = null,
    val picture: String? = null,
)
