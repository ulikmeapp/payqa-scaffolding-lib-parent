package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.enums.NotificationType
import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object NotificationRequestAssembler {

    fun toModel(request: NotificationRequest): NotificationModel {
        Validator.checkNotNull(request.content, NotificationRequest::content.name)
        Validator.checkNotEmpty(request.content, NotificationRequest::content.name)
        request.type?.let { type ->
            Validator.checkTrue(
                NotificationType.values().any { typeEnum -> typeEnum.name == type },
                "Invalid [${NotificationRequest::type.name}]. " +
                        "It must be between the following values: ${NotificationType.values().contentToString()}",
                "invalidType"
            )
        }
        return NotificationModel(
            content = request.content!!,
            type = request.type?.let { NotificationType.valueOf(it) } ?: NotificationType.SIMPLE,
            metadata = request.data
        )
    }

}