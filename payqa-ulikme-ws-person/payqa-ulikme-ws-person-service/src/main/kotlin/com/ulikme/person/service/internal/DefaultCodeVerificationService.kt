package com.ulikme.person.service.internal

import com.ulikme.person.infrastructure.persistence.mongo.entities.CodeVerificationEntity
import com.ulikme.person.infrastructure.persistence.mongo.repositories.CodeVerificationRepository
import com.ulikme.person.service.CodeVerificationService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.stereotype.Service

@Service
open class DefaultCodeVerificationService(
    private val repository: CodeVerificationRepository
) : CodeVerificationService {

    override fun findLatestByPersonId(personId: String): String =
        repository.findFirstByPersonIdAndEnabledOrderByCreatedDateDesc(personId)
            .orElseThrow { NotFoundException("Cannot find verification code for person with id: $personId") }
            .content

    override fun register(personId: String, type: String, code: String): String {
        repository.findAllByPersonIdAndEnabled(personId)
            .forEach { verificationCode ->
                verificationCode.enabled = false
                repository.save(verificationCode)
            }
        return repository.save(CodeVerificationEntity(
            personId = personId,
            type = type,
            content = code
        )).content
    }


}