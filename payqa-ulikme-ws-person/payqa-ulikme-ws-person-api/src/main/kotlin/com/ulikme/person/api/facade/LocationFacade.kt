package com.ulikme.person.api.facade

import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.domain.rr.request.LocationRequest

interface LocationFacade {

    fun register(id: String, request: LocationRequest): LocationModel

}