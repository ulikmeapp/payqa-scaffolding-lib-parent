package com.ulikme.interaction.infrastructure.communication.rest.client

import com.ulikme.interaction.infrastructure.communication.rest.api.PersonApi
import com.ulikme.person.domain.models.FullPersonModel
import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.domain.rr.request.SearchRequest
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import dev.payqa.scaffolding.apicrud.design.models.Data
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

    fun findByIdFull(id: String): FullPersonModel {
        log.info("[findByIdFull] init. [id=$id]")

        var output: FullPersonModel? = null

        super.manager.findByIdFull(SecurityContext.getToken(), id).subscribe(
            RxObserver(object : RetrofitCallback<FullPersonModel> {
                override fun onSuccess(response: FullPersonModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[findByIdFull] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to find person by id full.")
    }

    fun listOnly(request: SearchRequest, vararg id: String): Data<FullPersonModel> {
        log.info("[listOnly] init. [id=$id]")

        var output: Data<FullPersonModel>? = null

        super.manager.listOnly(
            SecurityContext.getToken(), request.lat, request.lng, request.dst, request.sm, request.age, *id
        ).subscribe(
            RxObserver(object : RetrofitCallback<Data<FullPersonModel>> {
                override fun onSuccess(response: Data<FullPersonModel>) {
                    output = response
                }

                override fun onInvalid(code: Int, message: String) {
                    log.error("[listOnly] error. [$message]")
                }
                override fun onFailed(message: String) {
                    log.error("[listOnly] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to list only persons by specified ids.")
    }

}