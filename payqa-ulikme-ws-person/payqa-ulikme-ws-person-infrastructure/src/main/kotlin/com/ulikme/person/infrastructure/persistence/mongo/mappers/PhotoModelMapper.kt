package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.PhotoEntity
import com.ulikme.person.infrastructure.persistence.mongo.entities.PhotoVerificationEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object PhotoModelMapper : ModelMapper<PhotoModel, PhotoEntity>() {

    override fun map(input: PhotoModel): PhotoEntity =
        PhotoEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            personId = input.personId,
            url = input.url,
            key = input.key,
            filename = input.filename,
            main = input.main
        )

    override fun inverseMap(input: PhotoEntity): PhotoModel =
        PhotoModel(
            id = input.id ?: "",
            personId = input.personId,
            url = input.url ?: "",
            key = input.key ?: "",
            filename = input.filename ?: "",
            main = input.main
        )

}

object PhotoVerificationModelMapper : ModelMapper<PhotoModel, PhotoVerificationEntity>() {

    override fun inverseMap(input: PhotoVerificationEntity): PhotoModel =
        PhotoModel(
            id = input.id ?: "",
            personId = input.personId,
            url = input.url ?: "",
            key = input.key ?: "",
            filename = input.filename ?: ""
        )

    override fun map(input: PhotoModel): PhotoVerificationEntity =
        PhotoVerificationEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            personId = input.personId,
            url = input.url,
            key = input.key,
            filename = input.filename
        )

}