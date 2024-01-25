package com.ulikme.establishment.infrastructure.persistence.mongo.mappers

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.country.infrastructure.persistence.mongo.mappers.CountryModelMapper
import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.utils.data.PointLocation
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object EstablishmentModelMapper : ModelMapper<EstablishmentModel, EstablishmentEntity>() {

    override fun inverseMap(input: EstablishmentEntity): EstablishmentModel =
        EstablishmentModel(
            id = input.id ?: "",
            name = input.name,
            country = input.country?.let { CountryModelMapper.inverseMap(it) } ?: CountryModel(),
            latitude = input.location.coordinates[1]!!,
            longitude = input.location.coordinates[0]!!,
            address = input.address,
            schedule = input.schedule
        )

    override fun map(input: EstablishmentModel): EstablishmentEntity =
        EstablishmentEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            name = input.name,
            country = CountryModelMapper.map(input.country),
            location = PointLocation(coordinates = arrayOf(input.longitude, input.latitude)),
            address = input.address,
            schedule = input.schedule
        )

}