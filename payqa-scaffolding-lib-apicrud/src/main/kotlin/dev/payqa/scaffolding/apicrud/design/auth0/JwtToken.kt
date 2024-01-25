package dev.payqa.scaffolding.apicrud.design.auth0

import com.google.gson.annotations.SerializedName
import java.util.*

data class JwtToken(

    val nickname: String = "",

    val name: String = "",

    @SerializedName("picture")
    val pictureUrl: String? = null,

    @SerializedName("updated_at")
    val updatedAt: Date? = null,

    val email: String = "",

    @SerializedName("email_verified")
    val emailVerified: Boolean = false,

    @SerializedName("iss")
    val issuer: String? = null,

    @SerializedName("aud")
    val audience: String? = null,

    val sub: String? = null,

    @SerializedName("exp")
    val expire: Int? = null

)
