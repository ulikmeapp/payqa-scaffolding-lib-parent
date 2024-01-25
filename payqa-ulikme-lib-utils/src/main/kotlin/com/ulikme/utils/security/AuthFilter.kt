package com.ulikme.utils.security

import com.ulikme.utils.http.support.RequestContext
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.design.auth0.JwtInterpreter
import dev.payqa.scaffolding.utils.security.JwtUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(2)
open class AuthFilter(
    @Value("\${auth0.jwk-url}") private val jwkUrl: String
) : Filter {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        if (request.headerNames.toList().any { headerName -> headerName.equals(HttpHeaders.AUTHORIZATION, true) }) {
            JwtUtils.getPureToken(
                request.getHeader(HttpHeaders.AUTHORIZATION),
                "Bearer"
            ).let { token -> SecurityContext.init(token, JwtInterpreter(jwkUrl).decode(token), request) }
            RequestContext.init(request)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

}