package com.ulikme.interaction.infrastructure.communication.rest.client

import com.ulikme.chat.domain.models.ChatModel
import com.ulikme.chat.domain.rr.request.ChatRequest
import com.ulikme.interaction.infrastructure.communication.rest.api.ChatApi
import com.ulikme.interaction.infrastructure.communication.rest.api.PersonApi
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class ChatRetrofitClient(
    baseUrl: String
) : RetrofitClient<ChatApi>(baseUrl)  {

    companion object {
        private val log = LoggerFactory.getLogger(PersonApi::class.java)
    }

    fun register(request: ChatRequest): ChatModel {
        log.info("[register] init. [request=$request]")

        var output: ChatModel? = null

        super.manager.register(SecurityContext.getToken(), request).subscribe(
            RxObserver(object : RetrofitCallback<ChatModel> {
                override fun onSuccess(response: ChatModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[register] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to register chat.")
    }


}