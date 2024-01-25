package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PreferencesModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.PreferencesRepository
import com.ulikme.person.service.PreferencesService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class DefaultPreferencesService(
    private val repository: PreferencesRepository
) : PreferencesService {

    @Cacheable("preferences-by-person", key = "#personId")
    override fun findByPersonId(personId: String): PreferencesModel =
        PreferencesModelMapper.inverseMap(
            repository.findByPersonId(personId)
                .orElseThrow { NotFoundException("Preferences not found for person with id: $personId") }
        )

    override fun findByPersonIdOrNull(personId: String): PreferencesModel? =
        repository.findByPersonId(personId).orElse(null)?.let {
            PreferencesModelMapper.inverseMap(it)
        }

    @CacheEvict("preferences-by-person", key = "#personId")
    override fun save(personId: String, preferences: PreferencesModel): PreferencesModel =
        PreferencesModelMapper.inverseMap(
            repository.save(
                PreferencesModelMapper.map(preferences).apply {
                    id = repository.findByPersonId(personId).orElse(null)?.id
                    this.personId = personId
                }
            )
        )

}