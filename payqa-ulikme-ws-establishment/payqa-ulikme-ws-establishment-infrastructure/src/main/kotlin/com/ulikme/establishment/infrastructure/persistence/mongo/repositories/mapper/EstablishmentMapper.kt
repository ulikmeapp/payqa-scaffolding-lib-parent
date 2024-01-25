package com.ulikme.establishment.infrastructure.persistence.mongo.repositories.mapper

import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity
import com.ulikme.establishment.infrastructure.persistence.mongo.entities.EstablishmentEntity
import com.ulikme.utils.data.PointLocation
import com.ulikme.utils.data.config.FIELD_ID
import org.bson.Document

object EstablishmentMapper {

    fun map(document: Document): EstablishmentEntity = EstablishmentEntity(
        id = document.getObjectId(FIELD_ID).toString(),
        name = document.getString(EstablishmentEntity::name.name),
        country = mapCountry(document.get(EstablishmentEntity::country.name, Document::class.java)),
        location = mapLocation(document.get(EstablishmentEntity::location.name, Document::class.java)),
        address = document.getString(EstablishmentEntity::address.name),
        schedule = document.getList(EstablishmentEntity::schedule.name, String::class.java)
    )

    private fun mapCountry(country: Document): CountryEntity = CountryEntity(
        code = country.getString(CountryEntity::code.name),
        name = country.getString(CountryEntity::name.name),
        phoneCode = country.getString(CountryEntity::phoneCode.name)
    )

    private fun mapLocation(location: Document): PointLocation = PointLocation(
        type = location.getString(PointLocation::type.name),
        coordinates = location.getList(PointLocation::coordinates.name, Double::class.javaObjectType).toTypedArray()
    )

}