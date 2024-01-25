package com.ulikme.interaction.infrastructure.persistence.mongo.mappers

import com.ulikme.interaction.domain.models.BlockingModel
import com.ulikme.interaction.infrastructure.persistence.mongo.entities.BlockingEntity
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PersonModelMapper
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object BlockingModelMapper : ModelMapper<BlockingModel, BlockingEntity>() {

    override fun map(input: BlockingModel): BlockingEntity =
        BlockingEntity(
            personId = input.personId,
            blockedPerson = input.blockedPerson?.let { PersonModelMapper.map(it) }
        )

    override fun inverseMap(input: BlockingEntity): BlockingModel =
        BlockingModel(
            personId = input.personId,
            blockedPerson = input.blockedPerson?.let { PersonModelMapper.inverseMap(it) }
        )

}