package com.ulikme.meet.infrastructure.communication.rest.client

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.meet.infrastructure.communication.rest.api.EstablishmentApi
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class EstablishmentRetrofitClient(baseUrl: String) : RetrofitClient<EstablishmentApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(EstablishmentApi::class.java)
    }

    fun findById(id: String): EstablishmentModel {
        log.info("[findById] init. [id=$id]")

        var output: EstablishmentModel? = null

        super.manager.findById(SecurityContext.getToken(), id).subscribe(
            RxObserver(object : RetrofitCallback<EstablishmentModel> {
                override fun onSuccess(response: EstablishmentModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[findById] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to find establishment by id.")
    }

}