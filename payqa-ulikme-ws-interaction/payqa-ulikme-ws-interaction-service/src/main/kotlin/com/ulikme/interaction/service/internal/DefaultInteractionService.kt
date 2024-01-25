package com.ulikme.interaction.service.internal

import com.ulikme.chat.domain.rr.request.ChatRequest
import com.ulikme.interaction.domain.enums.InteractionClassification
import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.infrastructure.communication.rest.client.ChatRetrofitClient
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.InteractionEntity
import com.ulikme.interaction.infrastructure.persistence.mongo.mappers.InteractionModelMapper
import com.ulikme.interaction.infrastructure.persistence.mongo.repositories.InteractionRepository
import com.ulikme.interaction.service.InteractionService
import com.ulikme.utils.common.DateTimeUtils
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Objects

@Service
open class DefaultInteractionService(
        private val repository: InteractionRepository
) : InteractionService {

    override fun paginateByPersonInteracted(request: PaginateRequest<InteractionModel>): Page<InteractionModel> =
        (request.params[InteractionModel::classification.name]
            ?.let { it as String }
            ?.takeIf { it.isNotEmpty() }
            ?.let { classification ->
                repository.findAllByPersonInteractedIdAndClassification(
                    request.params["personInteractedId"] as String,
                    classification,
                    request.forRepository(InteractionModel::class.java)
                )
            } ?: repository.findAllByPersonInteractedIdAndType(
                request.params["personInteractedId"] as String,
                request.params[InteractionModel::type.name] as String,
                request.forRepository(InteractionModel::class.java)
            )
        ).let { page -> Page.of(page.totalElements, InteractionModelMapper.inverseMap(page.content)) }

    override fun paginateByCreatedPerson(request: PaginateRequest<InteractionModel>): Page<InteractionModel> =
        (request.params[InteractionModel::classification.name]
            ?.let { it as String }
            ?.takeIf { it.isNotEmpty() }
            ?.let { classification ->
                repository.findAllByCreatedUserIdAndClassification(
                    request.params["createdPersonId"] as String,
                    classification,
                    request.forRepository(InteractionModel::class.java)
                )
            } ?: repository.findAllByCreatedUserIdAndType(
                request.params["createdPersonId"] as String,
                request.params[InteractionModel::type.name] as String,
                request.forRepository(InteractionModel::class.java
            ))
        ).let { page -> Page.of(page.totalElements, InteractionModelMapper.inverseMap(page.content)) }

    override fun paginateMatchesByPerson(request: PaginateRequest<InteractionModel>): Page<InteractionModel> =
        repository.findAllByCreatedUserIdAndMatch(
            request.params["createdPersonId"] as String,
            request.params[InteractionModel::match.name] as Boolean,
            request.forRepository(InteractionModel::class.java)
        ).let { page -> Page.of(page.totalElements, InteractionModelMapper.inverseMap(page.content)) }

    override fun listByPersonInteracted(personInteractedId: String): List<InteractionModel> =
        InteractionModelMapper.inverseMap(
            repository.findAllByPersonInteractedId(personInteractedId)
        )

    override fun listByCreatedPerson(createdPersonId: String): List<InteractionModel> =
        InteractionModelMapper.inverseMap(
            repository.findAllByCreatedUserId(createdPersonId)
        )

    override fun listMatchesUngroupeds(): List<InteractionModel> =
        InteractionModelMapper.inverseMap(
            repository.findAllByInGroupAndMatch(false)
        )

    override fun findById(id: String): InteractionModel =
        repository.findById(id).orElseThrow { throw NotFoundException("Cannot find interaction with id: $id") }
            .let { InteractionModelMapper.inverseMap(it) }

    override fun findByCoupleOrNull(personInteractedId: String, createdPersonId: String): InteractionModel? =
        repository.findByPersonInteractedIdAndCreatedUserId(personInteractedId, createdPersonId).orElse(null)
            ?.let { InteractionModelMapper.inverseMap(it) }

    override fun findByCoupleAndClassificationOrNull(
        personInteractedId: String,
        createdPersonId: String,
        classification: String
    ): InteractionModel? =
        repository.findByPersonInteractedIdAndCreatedUserIdAndClassification(
            personInteractedId, createdPersonId, classification
        ).orElse(null)?.let { InteractionModelMapper.inverseMap(it) }

    override fun countByCreatedPerson(createdPersonId: String, type: String?, classification: String?): Long =
        classification?.let {
            repository.countAllByCreatedUserIdAndClassificationAndCreatedDateBetween(
                createdPersonId,
                classification,
                DateTimeUtils.startOfDay(),
                DateTimeUtils.endOfDay()
            )
        } ?: type?.let {
            repository.countAllByCreatedUserIdAndTypeAndCreatedDateBetween(
                createdPersonId,
                type,
                DateTimeUtils.startOfDay(),
                DateTimeUtils.endOfDay()
            )
        } ?: repository.countAllByCreatedUserIdAndTypeAndCreatedDateBetween(
            createdPersonId,
            d0 = DateTimeUtils.startOfDay(),
            d1 = DateTimeUtils.endOfDay()
        )

    override fun register(interaction: InteractionModel): InteractionModel {
        val updatedInteraction = interaction.copy(
            match = isMatch(interaction),
            classification = if (isMatch(interaction)) InteractionClassification.ULIKME.value else interaction.classification
        )

        // Save the updatedInteraction to the repository
        return InteractionModelMapper.inverseMap(
            repository.save(
                InteractionModelMapper.map(updatedInteraction)
            )
        )
    }

    override fun joinToAGroup(id: String): InteractionModel =
        repository.findById(id).orElseThrow { throw NotFoundException("Cannot find interaction with id: $id") }
            .let { repository.save(it.apply { inGroup = true }) }
            .let { InteractionModelMapper.inverseMap(it) }

    override fun deleteById(id: String) =
        this.findById(id).let { interaction ->
            if (interaction.match) {
                throw ApiException(
                    HttpStatus.CONFLICT,
                    detail = ExceptionDetail(
                        message = "Cannot delete this interaction because it is a match.",
                        key = "interactionIsMatch"
                    )
                )
            }
            repository.deleteById(id)
        }

    override fun deleteBehaviorTypeByCreatedPerson(createdPersonId: String, classification: String?) =
        if (repository.existsByCreatedUserIdAndMatch(createdPersonId).not()) {
            classification?.let { interactionClassification ->
                repository.deleteByCreatedUserIdAndClassification(createdPersonId, interactionClassification)
            } ?: repository.deleteByCreatedUserIdAndType(createdPersonId)
        } else {
            throw ApiException(
                HttpStatus.CONFLICT,
                detail = ExceptionDetail(
                    message = "Cannot delete all interactions because there is a match at least.",
                    key = "matchesAmongInteractions"
                )
            )
        }

    private fun isMatch(interaction: InteractionModel): Boolean =
           /* interaction.classification?.let { classification ->

                classification == InteractionClassification.ULIKME.value ||
                repository.findByPersonInteractedIdAndCreatedUserIdAndClassification(
                    SecurityContext.getUser().id!!,
                    interaction.personInteracted!!.id,
                    InteractionClassification.LIKE.value
                ).let { inverseInteraction ->
                    (classification == InteractionClassification.LIKE.value && inverseInteraction.isPresent).apply {
                        if (this) {
                            inverseInteraction.ifPresent {
                                it.match = true
                                repository.save(it)
                            }
                        }
                    }
                }
            } ?: false*/


            interaction.classification?.let { classification ->
                when (classification) {
                    InteractionClassification.ULIKME.value -> return true
                    InteractionClassification.LIKE.value -> {

                        val likes: List<InteractionModel> = InteractionModelMapper.inverseMap(
                                repository.findAllByCreatedUserId(interaction.personInteracted!!.id)
                        )
                        for (like in likes) {
                            if (Objects.equals(like.personInteracted?.id, SecurityContext.getUser().id) && like.classification==InteractionClassification.LIKE.value) {
                                return true; }
                        }
                      return false;
                    }
                    else -> return false
                }
            } ?: false

}