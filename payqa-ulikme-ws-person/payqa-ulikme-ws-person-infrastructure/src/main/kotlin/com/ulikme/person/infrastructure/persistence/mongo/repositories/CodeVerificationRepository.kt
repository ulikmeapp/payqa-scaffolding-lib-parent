package com.ulikme.person.infrastructure.persistence.mongo.repositories

import com.ulikme.person.infrastructure.persistence.mongo.entities.CodeVerificationEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CodeVerificationRepository : MongoRepository<CodeVerificationEntity, String> {

    fun findFirstByPersonIdAndEnabledOrderByCreatedDateDesc(
        personId: String, enabled: Boolean = true
    ): Optional<CodeVerificationEntity>

    fun findAllByPersonIdAndEnabled(personId: String, enabled: Boolean = true): List<CodeVerificationEntity>

}