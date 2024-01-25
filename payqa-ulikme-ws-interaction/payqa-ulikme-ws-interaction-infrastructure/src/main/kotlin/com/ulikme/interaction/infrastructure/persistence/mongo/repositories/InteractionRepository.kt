package com.ulikme.interaction.infrastructure.persistence.mongo.repositories

import com.ulikme.interaction.domain.enums.InteractionType
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.InteractionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface InteractionRepository : MongoRepository<InteractionEntity, String> {

    fun findAllByPersonInteractedIdAndType(
        personInteractedId: String,
        type: String = InteractionType.BEHAVIOR.value,
        pageable: Pageable
    ): Page<InteractionEntity>

    fun findAllByPersonInteractedIdAndClassification(
        personInteractedId: String,
        classification: String,
        pageable: Pageable
    ): Page<InteractionEntity>

    fun findAllByPersonInteractedId(personInteractedId: String): List<InteractionEntity>

    fun findAllByPersonInteractedIdOrderByCreatedDateDesc(personInteractedId: String): List<InteractionEntity>

    fun findAllByCreatedUserIdAndType(
        createdUserId: String,
        type: String = InteractionType.BEHAVIOR.value,
        pageable: Pageable
    ): Page<InteractionEntity>

    fun findAllByCreatedUserIdAndClassification(
        createdUserId: String,
        classification: String,
        pageable: Pageable
    ): Page<InteractionEntity>

    fun findAllByCreatedUserIdAndMatch(
        createdUserId: String,
        isMatch: Boolean = true,
        pageable: Pageable
    ): Page<InteractionEntity>

    fun findAllByCreatedUserId(createdUserId: String): List<InteractionEntity>

    fun findAllByInGroupAndMatch(inGroup: Boolean = true, isMatch: Boolean = true): List<InteractionEntity>

    fun findByPersonInteractedIdAndCreatedUserId(
        personInteractedId: String,
        createdUserId: String
    ): Optional<InteractionEntity>

    fun findByPersonInteractedIdAndCreatedUserIdAndClassification(
        personInteractedId: String,
        createdUserId: String,
        classification: String
    ): Optional<InteractionEntity>

    fun countAllByCreatedUserIdAndTypeAndCreatedDateBetween(
        createdUserId: String,
        type: String = InteractionType.BEHAVIOR.value,
        d0: Date,
        d1: Date
    ): Long

    fun countAllByCreatedUserIdAndClassificationAndCreatedDateBetween(
        createdUserId: String,
        classification: String,
        d0: Date,
        d1: Date
    ): Long

    fun existsByCreatedUserIdAndMatch(createdUserId: String, isMatch: Boolean = true): Boolean

    fun deleteByCreatedUserIdAndType(createdUserId: String, type: String = InteractionType.BEHAVIOR.value)

    fun deleteByCreatedUserIdAndClassification(createdUserId: String, classification: String)

}