package com.ulikme.person.api.facade

import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest

interface NotificationFacade {

    fun send(id: String, request: NotificationRequest): NotificationModel

}