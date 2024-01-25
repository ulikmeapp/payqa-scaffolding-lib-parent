package com.ulikme.config.api.rest

import com.ulikme.config.service.ConfigService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1//config")
open class ConfigController(
    private val configService: ConfigService
) {

    @GetMapping("/app-properties")
    fun getAppProperties(): ResponseEntity<Map<String, Any>> =
        ResponseEntity.ok(configService.getAppProperties())

}