package com.ulikme.chat.infrastructure.communication.rest.client

import com.ulikme.chat.infrastructure.communication.rest.api.InteractionApi
import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class InteractionRetrofitClient(
    baseUrl: String
) : RetrofitClient<InteractionApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(InteractionApi::class.java)
    }

    fun findById(id: String): InteractionModel {
        log.info("[findById] init. [id=$id]")

        var output: InteractionModel? = null

        super.manager.findById(SecurityContext.getToken(), id).subscribe(
            RxObserver(object : RetrofitCallback<InteractionModel> {
                override fun onSuccess(response: InteractionModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[findById] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to find interaction by id.")
    }

}