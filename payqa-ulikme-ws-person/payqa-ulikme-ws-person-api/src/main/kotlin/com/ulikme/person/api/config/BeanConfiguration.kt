package com.ulikme.person.api.config

import com.ulikme.person.infrastructure.communication.rest.client.CountryRetrofitClient
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsS3Client
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsSqsClient
import dev.payqa.scaffolding.apicrud.communication.firebase.connector.FirebaseConnector
import dev.payqa.scaffolding.apicrud.design.auth0.JwtFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region

@Configuration
open class BeanConfiguration(
    @Value("\${backend.country.url}") private val backendCountryUrl: String,
    @Value("\${auth0.jwk-url}") private val jwkUrl: String,
    @Value("\${auth0.issuer}") private val issuer: String,
    @Value("\${firebase.service-account}") private val serviceAccount: String,
    @Value("\${cloud.aws.credantionals.access-key}") private val accessKey: String,
    @Value("\${cloud.aws.credantionals.secret-key}") private val secretKey: String,
) {

    @Bean
    fun countryRetrofitClient(): CountryRetrofitClient = CountryRetrofitClient(backendCountryUrl)

    @Bean
    fun awsS3Client(): AwsS3Client = AwsS3Client(Region.US_EAST_1, accessKey,secretKey)

    @Bean
    fun awsSqsClient(): AwsSqsClient = AwsSqsClient(Region.EU_NORTH_1, accessKey,secretKey)

    @Bean
    fun firebaseConnector(): FirebaseConnector = FirebaseConnector(serviceAccount)

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