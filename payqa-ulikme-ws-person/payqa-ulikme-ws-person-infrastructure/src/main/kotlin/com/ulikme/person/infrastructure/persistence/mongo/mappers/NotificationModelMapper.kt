package com.ulikme.person.infrastructure.persistence.mongo.mappers

import com.ulikme.person.domain.enums.NotificationType
import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.NotificationEntity
import dev.payqa.scaffolding.apicrud.design.models.mapper.ModelMapper

object NotificationModelMapper : ModelMapper<NotificationModel, NotificationEntity>() {

    override fun inverseMap(input: NotificationEntity): NotificationModel =
        NotificationModel(
            id = input.id ?: "",
            personId = input.personId,
            type = NotificationType.valueOf(input.type),
            content = input.content,
            date = input.date,
            metadata = input.metadata
        )

    override fun map(input: NotificationModel): NotificationEntity =
        NotificationEntity(
            id = input.id.takeIf { it.isNotEmpty() },
            personId = input.personId,
            type = input.type.name,
            content = input.content,
            date = input.date,
            metadata = input.metadata
        )

}