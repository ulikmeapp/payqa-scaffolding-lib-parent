package com.ulikme.country.api.config

import dev.payqa.scaffolding.apicrud.design.auth0.JwtFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BeanConfiguration(
    @Value("\${auth0.jwk-url}") private val jwkUrl: String,
    @Value("\${auth0.issuer}") private val issuer: String,
) {

}