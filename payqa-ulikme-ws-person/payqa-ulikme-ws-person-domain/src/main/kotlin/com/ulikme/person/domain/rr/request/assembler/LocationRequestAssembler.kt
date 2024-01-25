package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.domain.rr.request.LocationRequest
import dev.payqa.scaffolding.apicrud.data.utils.Validator

object LocationRequestAssembler {

    fun toModel(request: LocationRequest) : LocationModel {
        Validator.checkNotNull(request.latitude, LocationRequest::latitude.name)
        Validator.checkNotNull(request.longitude, LocationRequest::longitude.name)
        return LocationModel(
            latitude = request.latitude!!.toDouble(),
            longitude = request.longitude!!.toDouble()
        )
    }

}