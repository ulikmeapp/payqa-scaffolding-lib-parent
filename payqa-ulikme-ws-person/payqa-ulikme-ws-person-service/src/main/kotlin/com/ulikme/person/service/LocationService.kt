package com.ulikme.person.service

import com.ulikme.person.domain.models.LocationModel

interface LocationService {

    fun findLatestByPerson(personId: String): LocationModel?

    fun register(location: LocationModel) : LocationModel

}