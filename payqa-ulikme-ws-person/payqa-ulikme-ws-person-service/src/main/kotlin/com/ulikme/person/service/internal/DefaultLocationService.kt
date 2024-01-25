package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.infrastructure.persistence.mongo.mappers.LocationModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.LocationRepository
import com.ulikme.person.service.LocationService
import org.springframework.stereotype.Service

@Service
open class DefaultLocationService(
    private val repository: LocationRepository
) : LocationService {

    override fun findLatestByPerson(personId: String): LocationModel? =
        repository.findFirstByPersonIdOrderByCreatedDateDesc(personId).orElse(null)
            ?.let { LocationModelMapper.inverseMap(it) }


    override fun register(location: LocationModel): LocationModel =
        LocationModelMapper.inverseMap(
            repository.save(
                LocationModelMapper.map(location)
            )
        )

}