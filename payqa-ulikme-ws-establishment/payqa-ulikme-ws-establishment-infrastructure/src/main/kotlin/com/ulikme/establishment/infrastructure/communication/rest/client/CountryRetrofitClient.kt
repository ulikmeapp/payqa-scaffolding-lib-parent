package com.ulikme.establishment.infrastructure.communication.rest.client

import com.ulikme.country.domain.models.CountryModel
import com.ulikme.establishment.infrastructure.communication.rest.api.CountryApi
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitCallback
import dev.payqa.scaffolding.apicrud.communication.retrofit.RetrofitClient
import dev.payqa.scaffolding.apicrud.communication.retrofit.RxObserver
import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.slf4j.LoggerFactory

open class CountryRetrofitClient(
    baseUrl: String
) : RetrofitClient<CountryApi>(baseUrl) {

    companion object {
        private val log = LoggerFactory.getLogger(CountryApi::class.java)
    }

    fun findByCode(code: String): CountryModel {
        log.info("[findByCode] init. [code=$code]")

        var output: CountryModel? = null

        super.manager.findByCode(SecurityContext.getToken(), code).subscribe(
            RxObserver(object : RetrofitCallback<CountryModel> {
                override fun onSuccess(response: CountryModel) {
                    output = response
                }

                override fun onFailed(message: String) {
                    log.error("[findByCode] error. [$message]")
                }
            })
        )

        return output ?: throw InternalServerErrorException("Some error occurred trying to find country by code.")
    }

}