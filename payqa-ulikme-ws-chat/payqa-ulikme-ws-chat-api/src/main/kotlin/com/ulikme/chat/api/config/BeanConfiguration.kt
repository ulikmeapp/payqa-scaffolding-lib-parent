package com.ulikme.chat.api.config

import com.ulikme.chat.infrastructure.communication.rest.client.*
import dev.payqa.scaffolding.apicrud.design.auth0.JwtFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BeanConfiguration(
    @Value("\${backend.person.url}") private val backendPersonUrl: String,
    @Value("\${backend.interaction.url}") private val backendInteractionUrl: String,
    @Value("\${backend.establishment.url}") private val backendEstablishmentUrl: String,
    @Value("\${backend.meet.url}") private val backendMeetUrl: String,
    @Value("\${auth0.jwk-url}") private val jwkUrl: String,
    @Value("\${auth0.issuer}") private val issuer: String,
) {

    @Bean
    fun personRetrofitClient(): PersonRetrofitClient = PersonRetrofitClient(backendPersonUrl)

    @Bean
    fun establishmentRetrofitClient(): EstablishmentRetrofitClient = EstablishmentRetrofitClient(backendEstablishmentUrl)

    @Bean
    fun interactionRetrofitClient(): InteractionRetrofitClient = InteractionRetrofitClient(backendInteractionUrl)

    @Bean
    fun notificationRetrofitClient(): NotificationRetrofitClient = NotificationRetrofitClient(backendPersonUrl)

    @Bean
    fun meetRetrofitClient(): MeetRetrofitClient = MeetRetrofitClient(backendMeetUrl)

    @Bean
    fun groupMeetRetrofitClient(): GroupMeetRetrofitClient = GroupMeetRetrofitClient(backendMeetUrl)

}