package com.ulikme.chat.infrastructure.communication.rest.client

import com.ulikme.chat.infrastructure.communication.rest.api.PersonApi
import com.ulikme.person.domain.models.PersonModel
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class PersonRetrofitClient(
    baseUrl: String
) : RetrofitClient<PersonApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(PersonApi::class.java)
    }

    fun findById(id: String): PersonModel {
        log.info("[findById] init. [id=$id]")

        var output: PersonModel? = null

        SecurityContext

        super.manager.findById(SecurityContext.getToken(), id).subscribe(
            RxObserver(object : RetrofitCallback<PersonModel> {
                override fun onSuccess(response: PersonModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[findById] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to find person by id.")
    }

}