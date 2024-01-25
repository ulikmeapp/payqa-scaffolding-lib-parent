package com.ulikme.scheduler.communication.rest.client

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.scheduler.communication.rest.api.EstablishmentApi
import com.ulikme.scheduler.communication.rest.interceptor.Auth0Interceptor
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.slf4j.LoggerFactory

open class EstablishmentRetrofitClient(
    baseUrl: String
) : RetrofitClient<EstablishmentApi>(
    baseUrl,
    interceptors = arrayOf(Auth0Interceptor())
) {

    companion object {
        private val log = LoggerFactory.getLogger(EstablishmentApi::class.java)
    }

    fun paginateByLocation(
        latitude: Double,
        longitude: Double,
        request: PaginateRequest<EstablishmentModel>
    ): List<EstablishmentModel> {
        log.info("[paginateByLocation] init. [request=$request]")

        var output: List<EstablishmentModel> = mutableListOf()

        super.manager.paginateByLocation(
            latitude,
            longitude,
            request.page!!,
            request.pageSize!!,
            request.sortDirection,
            request.sortColumn
        ).subscribe(
            RxObserver(object : RetrofitCallback<Page<EstablishmentModel>> {
                override fun onSuccess(response: Page<EstablishmentModel>) {
                    output = response.items
                }

                override fun onFailed(message: String) {
                    log.error("[paginateByLocation] error. [$message]")
                }
            })
        )

        return output
    }

}