package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.infrastructure.persistence.mongo.mappers.NotificationModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.NotificationRepository
import com.ulikme.person.service.NotificationService
import org.springframework.stereotype.Service

@Service
open class DefaultNotificationService(
    private val repository: NotificationRepository
) : NotificationService{

    override fun register(notification: NotificationModel): NotificationModel =
        NotificationModelMapper.inverseMap(
            repository.save(
                NotificationModelMapper.map(notification)
            )
        )

}