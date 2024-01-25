package com.ulikme.person.api.facade.internal

import com.google.gson.Gson
import com.ulikme.person.api.facade.NotificationFacade
import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest
import com.ulikme.person.domain.rr.request.assembler.NotificationRequestAssembler
import com.ulikme.person.service.NotificationService
import com.ulikme.person.service.PreferencesService
import dev.payqa.scaffolding.apicrud.communication.firebase.expositor.MessagingExpositor
import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
open class DefaultNotificationFacade(
    private val notificationService: NotificationService,
    private val preferencesService: PreferencesService,
    private val messagingExpositor: MessagingExpositor
) : NotificationFacade {

    override fun send(id: String, request: NotificationRequest): NotificationModel =
        NotificationRequestAssembler.toModel(request)
            .copy(personId = id)
            .let { notification ->
                preferencesService.findByPersonId(id).firebaseTokens
                    .ifEmpty {
                        throw ApiException(
                            HttpStatus.CONFLICT,
                            detail = ExceptionDetail(
                                message = "User does not have a token at least to send notification",
                                key = "userWithoutTokens"
                            )
                        )
                    }
                    .let { firebaseTokens ->
                        notificationService.register(notification).apply {
                            if (date == null) {
                                messagingExpositor.send(
                                    title = "Ulikme",
                                    message = content,
                                    data = mutableMapOf<String, String>().apply {
                                        put(NotificationModel::type.name, type.name)
                                        metadata?.let { put(NotificationModel::metadata.name, Gson().toJson(it)) }
                                    },
                                    registrationTokens = firebaseTokens
                                )
                            }
                        }
                    }
            }

}