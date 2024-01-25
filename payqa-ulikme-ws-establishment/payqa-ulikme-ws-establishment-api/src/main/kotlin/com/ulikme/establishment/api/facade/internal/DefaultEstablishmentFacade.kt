package com.ulikme.establishment.api.facade.internal

import com.ulikme.establishment.api.facade.EstablishmentFacade
import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.domain.rr.request.EstablishmentRequest
import com.ulikme.establishment.domain.rr.request.assembler.EstablishmentRequestAssembler
import com.ulikme.establishment.infrastructure.communication.rest.client.CountryRetrofitClient
import com.ulikme.establishment.service.EstablishmentService
import org.springframework.stereotype.Component

@Component
open class DefaultEstablishmentFacade(
    private val establishmentService: EstablishmentService,
    private val countryRetrofitClient: CountryRetrofitClient
) : EstablishmentFacade {

    override fun register(request: EstablishmentRequest): EstablishmentModel =
        establishmentService.save(
            EstablishmentRequestAssembler.toModel(request).copy(
                country = countryRetrofitClient.findByCode(request.country!!)
            )
        )

    override fun update(id: String, request: EstablishmentRequest): EstablishmentModel =
        establishmentService.save(
            EstablishmentRequestAssembler.toModel(request).copy(
                id = id,
                country = countryRetrofitClient.findByCode(request.country!!)
            )
        )

}