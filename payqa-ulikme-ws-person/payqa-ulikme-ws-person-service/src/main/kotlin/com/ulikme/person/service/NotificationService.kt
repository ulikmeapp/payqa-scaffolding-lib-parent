package com.ulikme.person.service

import com.ulikme.person.domain.models.NotificationModel

interface NotificationService {

    fun register(notification: NotificationModel): NotificationModel

}