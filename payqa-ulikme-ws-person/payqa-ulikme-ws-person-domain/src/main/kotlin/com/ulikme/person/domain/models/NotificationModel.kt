package com.ulikme.person.domain.models

import com.ulikme.person.domain.enums.NotificationType
import dev.payqa.scaffolding.apicrud.design.models.Model

data class NotificationModel(
    val id: String = "",
    val personId: String = "",
    val type: NotificationType = NotificationType.SIMPLE,
    val content: String = "",
    val metadata: Any? = null,
    val date: String? = null
) : Model()