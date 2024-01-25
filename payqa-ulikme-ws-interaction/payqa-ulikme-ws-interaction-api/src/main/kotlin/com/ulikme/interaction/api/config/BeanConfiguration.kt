package com.ulikme.interaction.api.config

import com.ulikme.interaction.infrastructure.communication.rest.client.ChatRetrofitClient
import com.ulikme.interaction.infrastructure.communication.rest.client.PersonRetrofitClient
import dev.payqa.scaffolding.apicrud.design.auth0.JwtFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BeanConfiguration(
    @Value("\${backend.person.url}") private val backendPersonUrl: String,
    @Value("\${backend.chat.url}") private val backendChatUrl: String,
    @Value("\${auth0.jwk-url}") private val jwkUrl: String,
    @Value("\${auth0.issuer}") private val issuer: String,
) {

    @Bean
    fun personRetrofitClient(): PersonRetrofitClient = PersonRetrofitClient(backendPersonUrl)

    @Bean
    fun chatRetrofitClient(): ChatRetrofitClient = ChatRetrofitClient(backendPersonUrl)

    /**
     * Register jwt filter
     * (It can be injected also by @ComponentScan instead of this,
     * not is injecting manually because we need to see Swagger UI)
     */
    @Bean
    fun jwtFilter(): FilterRegistrationBean<JwtFilter> =
        FilterRegistrationBean<JwtFilter>().apply {
            filter = JwtFilter(jwkUrl, issuer)
            addUrlPatterns("/api/*")
        }

}