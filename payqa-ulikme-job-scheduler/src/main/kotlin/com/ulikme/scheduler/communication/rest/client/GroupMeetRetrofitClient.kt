package com.ulikme.scheduler.communication.rest.client

import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.scheduler.communication.rest.api.GroupMeetApi
import com.ulikme.scheduler.communication.rest.interceptor.Auth0Interceptor
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class GroupMeetRetrofitClient(
    baseUrl: String
) : RetrofitClient<GroupMeetApi>(
    baseUrl,
    interceptors = arrayOf(Auth0Interceptor())
) {

    companion object {
        private val log = LoggerFactory.getLogger(GroupMeetApi::class.java)
    }

    fun register(request: MeetRequest): MeetModel {
        log.info("[register] init. [request=$request]")

        var output: MeetModel? = null

        super.manager.register(request).subscribe(
            RxObserver(object : RetrofitCallback<MeetModel> {
                override fun onSuccess(response: MeetModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[register] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to register group meeting.")
    }

}