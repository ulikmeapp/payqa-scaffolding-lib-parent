package com.ulikme.scheduler.config

import com.ulikme.scheduler.communication.rest.auth0.enums.GrantType
import com.ulikme.scheduler.communication.rest.auth0.request.TokenRequest
import com.ulikme.scheduler.communication.rest.client.Auth0RetrofitClient
import com.ulikme.scheduler.communication.rest.client.EstablishmentRetrofitClient
import com.ulikme.scheduler.communication.rest.client.GroupMeetRetrofitClient
import com.ulikme.scheduler.communication.rest.client.MessageRetrofitClient
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsSqsClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region

@Configuration
open class BeanConfiguration(
    @Value("\${auth0.url}") private val auth0Url: String,
    @Value("\${auth0.client-id}") private val auth0ClientId: String,
    @Value("\${auth0.client-secret}") private val auth0ClientSecret: String,
    @Value("\${auth0.audience}") private val auth0Audience: String,
    @Value("\${backend.meet.url}") private val backendMeetUrl: String,
    @Value("\${backend.establishment.url}") private val backendEstablishmentUrl: String,
    @Value("\${backend.chat.url}") private val backendChatUrl: String,
    @Value("\${cloud.aws.credantionals.access-key}") private val accessKey: String,
    @Value("\${cloud.aws.credantionals.secret-key}") private val secretKey: String,
) {

    @Bean
    fun awsSqsClient(): AwsSqsClient = AwsSqsClient(Region.EU_NORTH_1,accessKey,secretKey)

    @Bean
    fun auth0RetrofitClient(): Auth0RetrofitClient = Auth0RetrofitClient(auth0Url)

    @Bean
    fun establishmentRetrofitClient(): EstablishmentRetrofitClient = EstablishmentRetrofitClient(backendEstablishmentUrl)

    @Bean
    fun messageRetrofitClient(): MessageRetrofitClient = MessageRetrofitClient(backendChatUrl)

    @Bean
    fun groupMeetRetrofitClient(): GroupMeetRetrofitClient = GroupMeetRetrofitClient(backendMeetUrl)

    @Bean
    fun tokenRequest(): TokenRequest =
        TokenRequest(
            client_id = auth0ClientId,
            client_secret = auth0ClientSecret,
            audience = auth0Audience,
            grant_type = GrantType.CLIENT_CREDENTIALS.key
        )

}