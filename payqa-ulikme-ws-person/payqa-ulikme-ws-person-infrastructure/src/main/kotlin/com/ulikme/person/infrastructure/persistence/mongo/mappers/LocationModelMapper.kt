package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.LocationEntity
import com.ulikme.person.infrastructure.persistence.mongo.enums.LocationType
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object LocationModelMapper : ModelMapper<LocationModel, LocationEntity>() {

    override fun inverseMap(input: LocationEntity): LocationModel =
        LocationModel(
            id = input.id ?: "",
            personId = input.personId,
            longitude = input.coordinates[0],
            latitude = input.coordinates[1]
        )

    override fun map(input: LocationModel): LocationEntity =
        LocationEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            personId = input.personId,
            coordinates = arrayOf(input.longitude, input.latitude),
            type = LocationType.POINT.value
        )

}