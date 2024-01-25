package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.infrastructure.persistence.mongo.entities.PersonEntity
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PersonModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.FullPersonRepository
import com.ulikme.person.infrastructure.persistence.mongo.repositories.PersonRepository
import com.ulikme.person.service.PersonService
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
open class DefaultPersonService(
    private val repository: PersonRepository,
    private val fullPersonRepository: FullPersonRepository,
) : PersonService {

    @Cacheable("person-by-id", key = "#id")
    override fun findById(id: String): PersonModel =
        PersonModelMapper.inverseMap(
            repository.findById(id)
                .orElseThrow { NotFoundException("Person not found with id: $id") }
        )

    override fun findByEmail(email: String): PersonModel =
        PersonModelMapper.inverseMap(
            repository.findByEmail(email)
                .orElseThrow { NotFoundException("Person not found with email: $email") }
        )

    override fun findByEmailOrNull(email: String): PersonModel? =
        repository.findByEmail(email).orElse(null)?.let {
            PersonModelMapper.inverseMap(it)
        }

    @CacheEvict("person-by-id", key = "#person.id")
    override fun save(person: PersonModel): PersonModel =
        PersonModelMapper.inverseMap(
            repository.save(
                this.prepareToSave(person)
            )
        )

    @Caching(
        evict = [
            CacheEvict("person-by-id", key = "#id"),
            CacheEvict("full-person-by-id", key = "#id")
        ]
    )
    override fun delete(id: String) =
        with(this.findById(id)) {
            repository.deleteById(this.id)
            fullPersonRepository.deleteById(this.id)
        }

    override fun deleteByEmail(email: String) =
        with(this.findByEmail(email)) {
            repository.deleteByEmail(this.personal.email)
        }

    private fun prepareToSave(person: PersonModel): PersonEntity =
        PersonModelMapper.map(person.apply { calculateProfilePercentage() })

}