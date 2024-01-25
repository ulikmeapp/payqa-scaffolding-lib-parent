package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.person.domain.models.InterestModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.InterestEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object InterestModelMapper : ModelMapper<InterestModel, InterestEntity>() {

    override fun map(input: InterestModel): InterestEntity =
        InterestEntity(
            id = input.id.takeIf { it.isNotBlank() },
            name = input.name,
            description = input.description,
            showInOnBoarding = input.showInOnBoarding
        )

    override fun inverseMap(input: InterestEntity): InterestModel =
        InterestModel(
            id = input.id ?: "",
            name = input.name,
            description = input.description,
            showInOnBoarding = input.showInOnBoarding
        )

}