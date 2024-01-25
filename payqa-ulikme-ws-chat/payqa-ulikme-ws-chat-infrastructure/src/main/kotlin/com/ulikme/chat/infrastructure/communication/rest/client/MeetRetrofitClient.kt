package com.ulikme.chat.infrastructure.communication.rest.client

import com.ulikme.chat.infrastructure.communication.rest.api.MeetApi
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class MeetRetrofitClient(baseUrl: String) : RetrofitClient<MeetApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(MeetApi::class.java)
    }

    fun register(request: MeetRequest): MeetModel {
        log.info("[register] init. [request=$request]")

        var output: MeetModel? = null
        var errorMessage: String? = null

        super.manager.register(SecurityContext.getToken(), request).subscribe(
            RxObserver(object : RetrofitCallback<MeetModel> {
                override fun onSuccess(response: MeetModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    errorMessage = message
                    log.error("[register] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException(errorMessage ?: "Some error occurred trying to register meet.")
    }

}