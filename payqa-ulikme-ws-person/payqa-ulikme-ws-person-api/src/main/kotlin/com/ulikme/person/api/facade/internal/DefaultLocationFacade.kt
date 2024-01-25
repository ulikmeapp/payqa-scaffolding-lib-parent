package com.ulikme.person.api.facade.internal

import com.ulikme.person.api.facade.LocationFacade
import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.domain.rr.request.LocationRequest
import com.ulikme.person.domain.rr.request.assembler.LocationRequestAssembler
import com.ulikme.person.service.FullPersonService
import com.ulikme.person.service.LocationService
import com.ulikme.utils.http.support.SecurityContext
import org.springframework.stereotype.Service

@Service
open class DefaultLocationFacade(
    private val locationService: LocationService,
    private val fullPersonService: FullPersonService
) : LocationFacade {

    override fun register(id: String, request: LocationRequest): LocationModel =
        LocationRequestAssembler.toModel(request).let { locationFromRequest ->
            locationService.register(
                locationFromRequest.copy(personId = id)
            )
        }.also { fullPersonService.updateLatestLocation(it.personId, it) }

}