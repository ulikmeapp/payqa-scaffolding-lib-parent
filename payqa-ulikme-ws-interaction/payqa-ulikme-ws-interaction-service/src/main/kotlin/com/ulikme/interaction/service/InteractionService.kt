package com.ulikme.interaction.service

import com.ulikme.interaction.domain.models.InteractionModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest

interface InteractionService {

    fun paginateByPersonInteracted(request: PaginateRequest<InteractionModel>): Page<InteractionModel>

    fun paginateByCreatedPerson(request: PaginateRequest<InteractionModel>): Page<InteractionModel>

    fun paginateMatchesByPerson(request: PaginateRequest<InteractionModel>): Page<InteractionModel>

    fun listByPersonInteracted(personInteractedId: String): List<InteractionModel>

    fun listByCreatedPerson(createdPersonId: String): List<InteractionModel>

    fun listMatchesUngroupeds(): List<InteractionModel>

    fun findById(id: String): InteractionModel

    fun findByCoupleOrNull(personInteractedId: String, createdPersonId: String): InteractionModel?

    fun findByCoupleAndClassificationOrNull(
        personInteractedId: String,
        createdPersonId: String,
        classification: String
    ): InteractionModel?

    fun countByCreatedPerson(createdPersonId: String, type: String? = null, classification: String? = null): Long

    fun register(interaction: InteractionModel): InteractionModel

    fun joinToAGroup(id: String): InteractionModel

    fun deleteById(id: String)

    fun deleteBehaviorTypeByCreatedPerson(createdPersonId: String, classification: String?)

}