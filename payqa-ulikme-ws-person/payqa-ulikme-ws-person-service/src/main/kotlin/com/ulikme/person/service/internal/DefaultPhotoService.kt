package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PhotoModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.mappers.PhotoVerificationModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.PhotoRepository
import com.ulikme.person.infrastructure.persistence.mongo.repositories.PhotoVerificationRepository
import com.ulikme.person.service.PhotoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
open class DefaultPhotoService(
    private val repository: PhotoRepository,
    private val verificationRepository: PhotoVerificationRepository,
    @Value("\${app.config.max-photos}") private val maxPhotos: Int
) : PhotoService {

    @Cacheable("photos-by-person", key = "#personId + #verification")
    override fun listByPerson(personId: String, verification: Boolean): List<PhotoModel> =
        when (verification) {
            true ->
                PhotoVerificationModelMapper.inverseMap(
                    verificationRepository.findAllByPersonIdOrderByCreatedDateDesc(
                        personId, PageRequest.of(0, maxPhotos)
                    ).content
                )
            false ->
                PhotoModelMapper.inverseMap(
                    repository.findAllByPersonIdOrderByCreatedDateDesc(
                        personId, PageRequest.of(0, maxPhotos)
                    ).content
                )
        }

    @Cacheable("main-by-person", key = "#personId")
    override fun findMainByPersonOrNull(personId: String): PhotoModel? =
        repository.findByPersonIdAndMain(personId).orElse(null)
            ?.let { PhotoModelMapper.inverseMap(it) }

    override fun findByPersonAndNameOrNull(personId: String, name: String): PhotoModel? =
        repository.findByPersonIdAndFilename(personId, name).orElse(null)
            .let { PhotoModelMapper.inverseMap(it) }

    @Caching(
        evict = [
            CacheEvict("photos-by-person", key = "#photo.personId + #isVerification"),
            CacheEvict("main-by-person", key = "#photo.personId")
        ]
    )
    override fun register(photo: PhotoModel, isVerification: Boolean): PhotoModel =
        when (isVerification) {
            true -> PhotoVerificationModelMapper.inverseMap(
                verificationRepository.save(
                    PhotoVerificationModelMapper.map(photo)
                )
            )
            false -> photo.copy(main = true).let { mainPhoto ->
                repository.findByPersonIdAndMain(mainPhoto.personId).ifPresent {
                    it.main = false
                    repository.save(it)
                }
                PhotoModelMapper.inverseMap(
                    repository.save(
                        PhotoModelMapper.map(mainPhoto)
                    )
                )
            }
        }

    override fun delete(id: String) = repository.deleteById(id)

    @Caching(
        evict = [
            CacheEvict("photos-by-person", key = "#personId"),
            CacheEvict("main-by-person", key = "#personId")
        ]
    )
    override fun deleteByName(personId: String, name: String) =
        repository.deleteByPersonIdAndFilename(personId, name)

}