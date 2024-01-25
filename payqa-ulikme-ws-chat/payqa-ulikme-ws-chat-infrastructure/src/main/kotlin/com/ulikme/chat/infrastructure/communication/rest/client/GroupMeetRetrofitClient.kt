package com.ulikme.chat.infrastructure.communication.rest.client

import com.ulikme.chat.infrastructure.communication.rest.api.GroupMeetApi
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class GroupMeetRetrofitClient(baseUrl: String) : RetrofitClient<GroupMeetApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(GroupMeetApi::class.java)
    }

    fun confirm(chatId: String) {
        log.info("[confirm] init. [chatId=$chatId]")

        var accepted: Boolean = false

        super.manager.confirm(SecurityContext.getToken(), chatId).subscribe(
            RxObserver(object : RetrofitCallback<Void> {
                override fun onAccepted() {
                    accepted = true
                }

                override fun onFailed(message: String) {
                    log.error("[register] error. [$message]")
                }
            })
        )

        if (!accepted)  {
            throw InternalServerErrorException("Some error occurred trying to confirm group meet.")
        }
    }

}