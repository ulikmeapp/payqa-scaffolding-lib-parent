package com.ulikme.country.infrastructure.persistence.mongo.mappers

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object CountryModelMapper : ModelMapper<CountryModel, CountryEntity>() {

    override fun map(input: CountryModel): CountryEntity =
        CountryEntity(
            code = input.code,
            name = input.name,
            phoneCode = input.phoneCode
        )

    override fun inverseMap(input: CountryEntity): CountryModel =
        CountryModel(
            code = input.code,
            name = input.name,
            phoneCode = input.phoneCode
        )

}