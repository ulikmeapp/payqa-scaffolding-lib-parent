package com.ulikme.config.service.internal

import com.ulikme.config.infrastructure.persistence.mongo.repositories.ConfigRepository
import com.ulikme.config.service.ConfigService
import org.springframework.stereotype.Service

@Service
open class DefaultConfigService(
    private val repository: ConfigRepository
) : ConfigService {

    override fun getAppProperties(): Map<String, Any> =
        repository.findAll().firstOrNull()
            ?.appProperties ?: emptyMap()

}