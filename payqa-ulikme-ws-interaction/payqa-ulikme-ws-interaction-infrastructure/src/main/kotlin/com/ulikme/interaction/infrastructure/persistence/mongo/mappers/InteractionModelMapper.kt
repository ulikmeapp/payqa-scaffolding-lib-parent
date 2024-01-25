package com.ulikme.interaction.infrastructure.persistence.mongo.mappers

import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.InteractionEntity
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PersonModelMapper
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object InteractionModelMapper : ModelMapper<InteractionModel, InteractionEntity>() {

    override fun inverseMap(input: InteractionEntity): InteractionModel =
        InteractionModel(
            id = input.id ?: "",
            type = input.type,
            classification = input.classification,
            zone = input.zone,
            personInteracted = input.personInteracted?.let { PersonModelMapper.inverseMap(it) },
            match = input.match,
            inGroup = input.inGroup,
            createdPersonId = input.createdUser?.id
        )

    override fun map(input: InteractionModel): InteractionEntity =
        InteractionEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            type = input.type,
            classification = input.classification,
            zone = input.zone,
            personInteracted = input.personInteracted?.let { PersonModelMapper.map(it) },
            match = input.match,
            inGroup = input.inGroup,
        )

}