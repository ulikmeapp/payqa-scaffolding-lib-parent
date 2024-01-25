package com.ulikme.establishment.domain.rr.request.assembler

import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.domain.rr.request.EstablishmentRequest
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object EstablishmentRequestAssembler {

    fun toModel(request: EstablishmentRequest): EstablishmentModel {
        Validator.checkNotNull(request.name, EstablishmentRequest::name.name)
        Validator.checkNotEmpty(request.name, EstablishmentRequest::name.name)
        Validator.checkNotNull(request.country, EstablishmentRequest::country.name)
        Validator.checkNotEmpty(request.country, EstablishmentRequest::country.name)
        Validator.checkNotNull(request.schedule, EstablishmentRequest::schedule.name)
        Validator.checkFalse(request.schedule!!.isEmpty(), "Establishment must have 1 schedule at least")
        Validator.checkNotNull(request.latitude, EstablishmentRequest::latitude.name)
        Validator.checkNotNull(request.longitude, EstablishmentRequest::longitude.name)
        return EstablishmentModel(
            name = request.name!!,
            latitude = request.latitude!!.toDouble(),
            longitude = request.longitude!!.toDouble(),
            address = request.address,
            schedule = request.schedule
        )
    }

}