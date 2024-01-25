package com.ulikme.establishment.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig {

    companion object {
        private val GET_DELETE_RESPONSES = listOf(
            ResponseMessageBuilder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.reasonPhrase)
                .build()
        )
        private val POST_RESPONSES = listOf(
            ResponseMessageBuilder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.reasonPhrase)
                .build(),
            ResponseMessageBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
                .build(),
        )
        private val PATCH_RESPONSES = listOf(
            ResponseMessageBuilder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.reasonPhrase)
                .build(),
            ResponseMessageBuilder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.reasonPhrase)
                .build(),
            ResponseMessageBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
                .build(),
        )
    }

    /**
     * Initialize swagger api docs,
     * registering auth0 jwt token as security scheme
     */
    @Bean
    open fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .globalResponseMessage(RequestMethod.GET, GET_DELETE_RESPONSES)
            .globalResponseMessage(RequestMethod.POST, POST_RESPONSES)
            .globalResponseMessage(RequestMethod.PATCH, PATCH_RESPONSES)
            .globalResponseMessage(RequestMethod.DELETE, GET_DELETE_RESPONSES)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.ulikme.establishment.api.rest"))
            .paths(PathSelectors.regex("/api.*"))
            .build()
            .securityContexts(mutableListOf(
                SecurityContext.builder()
                    .securityReferences(listOf(
                        SecurityReference(
                            "JWT",
                            arrayOf(AuthorizationScope("global", "accessEverything"))
                        )
                    ))
                    .build()
            ))
            .securitySchemes(mutableListOf(
                ApiKey("Auth0 - JWT", "Authorization", "header")
            ))

}