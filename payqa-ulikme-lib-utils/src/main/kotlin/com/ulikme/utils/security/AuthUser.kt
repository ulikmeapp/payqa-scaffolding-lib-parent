package com.ulikme.utils.security

import dev.payqa.scaffolding.apicrud.design.auth0.JwtToken

private val ULIKME_USER = AuthUser(
    name = "Ulikme",
    email = "backend@ulikme.com",
    platform = "Backend"
)

data class AuthUser(
    val id: String? = null,
    val name: String = "",
    val email: String = "",
    val platform: String? = null,
    val timezone: String? = null
) {

    constructor(jwtToken: JwtToken, timezone: String?) : this(
        id = jwtToken.sub?.let { it.split("|")[1] },
        name = jwtToken.name,
        email = jwtToken.email,
        platform = jwtToken.sub?.let { it.split("|")[0] },
        timezone = timezone
    )

    constructor(timezone: String?) : this(
        name = ULIKME_USER.name,
        email = ULIKME_USER.email,
        platform = ULIKME_USER.platform,
        timezone = timezone
    )

}