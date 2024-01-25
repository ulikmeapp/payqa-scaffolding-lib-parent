package com.ulikme.scheduler.communication.rest.client

import com.ulikme.chat.domain.models.MessageModel
import com.ulikme.chat.domain.rr.request.MessageRequest
import com.ulikme.scheduler.communication.rest.api.MessageApi
import com.ulikme.scheduler.communication.rest.interceptor.Auth0Interceptor
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class MessageRetrofitClient(
    baseUrl: String
) : RetrofitClient<MessageApi>(
    baseUrl,
    interceptors = arrayOf(Auth0Interceptor())
) {

    companion object {
        private val log = LoggerFactory.getLogger(MessageApi::class.java)
    }

    fun register(chatId: String, request: MessageRequest): MessageModel {
        log.info("[register] init. [request=$request]")

        var output: MessageModel? = null

        super.manager.register(chatId, request).subscribe(
            RxObserver(object : RetrofitCallback<MessageModel> {
                override fun onSuccess(response: MessageModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[register] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to register message.")
    }

}