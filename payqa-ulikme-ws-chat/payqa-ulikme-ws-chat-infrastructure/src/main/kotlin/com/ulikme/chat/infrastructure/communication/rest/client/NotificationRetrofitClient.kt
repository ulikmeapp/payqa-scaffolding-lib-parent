package com.ulikme.chat.infrastructure.communication.rest.client

import com.ulikme.chat.infrastructure.communication.rest.api.NotificationApi
import com.ulikme.person.domain.models.NotificationModel
import com.ulikme.person.domain.rr.request.NotificationRequest
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class NotificationRetrofitClient(
    baseUrl: String
) : RetrofitClient<NotificationApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(NotificationApi::class.java)
    }

    fun send(id: String, request: NotificationRequest): NotificationModel {
        log.info("[send] init. [id=$id]")

        var output: NotificationModel? = null

        super.manager.send(SecurityContext.getToken(), id, request).subscribe(
            RxObserver(object : RetrofitCallback<NotificationModel> {
                override fun onSuccess(response: NotificationModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[send] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to send notification.")
    }

}