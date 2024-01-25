package com.ulikme.utils.http.support

import com.ulikme.utils.security.AuthUser
import dev.payqa.scaffolding.apicrud.design.auth0.JwtToken
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.springframework.web.servlet.support.RequestContextUtils
import javax.servlet.http.HttpServletRequest

open class SecurityContext
private constructor(
    val token: String,
    val jwtToken: JwtToken,
    val request: HttpServletRequest
) {

    companion object {
        private var instance: SecurityContext? = null

        fun init(token: String, jwtToken: JwtToken, request: HttpServletRequest) {
            if (instance == null || instance!!.token != token)
                instance = SecurityContext(token, jwtToken, request)
        }

        fun getToken(): String = instance?.token?.let { "Bearer $it" }
            ?: throw InternalServerErrorException(NOT_INITIALIZED_CONTEXT)

        fun getDecodedToken(): JwtToken = instance?.jwtToken
            ?: throw InternalServerErrorException(NOT_INITIALIZED_CONTEXT)

        fun getUser(): AuthUser = instance?.let { context ->
            context.jwtToken.let { decodedJwtToken ->
                val timeZone = RequestContextUtils.getTimeZone(context.request)?.id
                when (decodedJwtToken.sub?.contains("|") ?: false) {
                    true -> AuthUser(decodedJwtToken, timeZone)
                    false -> AuthUser(timeZone)
                }
            }
        } ?: throw InternalServerErrorException(NOT_INITIALIZED_CONTEXT)
    }

}