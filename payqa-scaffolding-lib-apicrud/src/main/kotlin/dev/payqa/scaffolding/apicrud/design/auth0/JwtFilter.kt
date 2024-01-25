package dev.payqa.scaffolding.apicrud.design.auth0

import com.google.gson.Gson
import dev.payqa.scaffolding.apicrud.design.exceptions.http.BadRequestException
import dev.payqa.scaffolding.apicrud.design.rest.rr.response.ErrorResponse
import dev.payqa.scaffolding.utils.security.JwtUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class JwtFilter(
    @Value("\${auth0.jwk-url}") private val jwkUrl: String,
    @Value("\${auth0.issuer}") private val issuer: String,
) : Filter {

    private val jwtInterpreter = JwtInterpreter(jwkUrl)

    override fun doFilter(
        servletRequest: ServletRequest?,
        servletResponse: ServletResponse?,
        filterChain: FilterChain?
    ) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        try {
            if (!request.headerNames.toList().any { headerName -> headerName.equals(HttpHeaders.AUTHORIZATION, true) }) {
                throw BadRequestException("Authorization token is not present into request.")
            }
            jwtInterpreter.verifyToken(
                JwtUtils.getPureToken(request.getHeader(HttpHeaders.AUTHORIZATION), "Bearer"),
                issuer = issuer
            )
            filterChain?.doFilter(request, response)
        } catch (exception: BadRequestException) {
            with(response) {
                status = exception.httpStatus.value()
                contentType = MediaType.APPLICATION_JSON_VALUE
                writer.write(Gson().toJson(
                    ErrorResponse(
                        message = exception.detail.message,
                        code = exception.httpStatus.value(),
                        key = exception.detail.key
                    )
                ))
            }
        }
    }

}