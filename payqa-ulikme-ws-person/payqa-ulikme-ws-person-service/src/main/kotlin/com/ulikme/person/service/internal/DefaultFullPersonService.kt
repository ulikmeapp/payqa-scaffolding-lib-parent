package com.ulikme.person.service.internal

import com.ulikme.person.domain.models.*
import com.ulikme.person.domain.rr.request.SearchRequest
import com.ulikme.person.infrastructure.communication.aws.enums.AwsQueue
import com.ulikme.person.infrastructure.persistence.mongo.mappers.FullPersonModelMapper
import com.ulikme.person.infrastructure.persistence.mongo.repositories.FullPersonRepository
import com.ulikme.person.service.FullPersonService
import com.ulikme.person.service.assembler.FullPersonAssembler
import com.ulikme.utils.rx.RxBgObserver
import dev.payqa.scaffolding.apicrud.communication.aws.client.AwsSqsClient
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException
import dev.payqa.scaffolding.apicrud.design.models.Data
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import java.util.concurrent.Callable

@Service
open class DefaultFullPersonService(
    private val repository: FullPersonRepository,
    private val awsSqsClient: AwsSqsClient
) : FullPersonService {

    override fun paginate(request: PaginateRequest<FullPersonModel>): Page<FullPersonModel> =
        request.forRepository(FullPersonModel::class.java).let { pageable ->
            when (request.params.keys.isEmpty()) {
                true -> repository.findAll(pageable)
                false -> repository.findAll(request.params, pageable)
            }
        }.let { page -> Page.of(page.totalElements, FullPersonModelMapper.inverseMap(page.content)) }

    override fun listOnly(request: SearchRequest, vararg includedIds: String): Data<FullPersonModel> =
        Data.of(
            FullPersonModelMapper.inverseMap(
                repository.findOnly(request.toMap(), *includedIds)
            )
        )

    @Cacheable("full-person-by-id", key = "#id")
    override fun findById(id: String): FullPersonModel =
        repository.findById(id)
            .orElseThrow { throw NotFoundException("Full person with id $id does not exists.") }
            .let { FullPersonModelMapper.inverseMap(it) }

    override fun findByIdOrNull(id: String): FullPersonModel? =
        repository.findById(id).orElse(null)?.let { FullPersonModelMapper.inverseMap(it) }

    @CacheEvict("full-person-by-id", key = "#fullPerson.id")
    override fun save(fullPerson: FullPersonModel): Subscription =
        envolveInNewThread({ persist(fullPerson) })

    override fun save(
        person: PersonModel,
        photos: List<PhotoModel>?,
        preferences: PreferencesModel?,
        latestLocation: LocationModel?
    ): Subscription =
        envolveInNewThread({
            persist(
                this.findByIdOrNull(person.id)?.let { fullPersonFound ->
                    FullPersonAssembler.fromPerson(
                        person,
                        photos = photos ?: fullPersonFound.photos,
                        preferences = preferences ?: fullPersonFound.preferences,
                        latestLocation = latestLocation ?: fullPersonFound.latestLocation
                    )
                } ?: FullPersonAssembler.fromPerson(person, photos, preferences, latestLocation)
            )
        })

    @CacheEvict("full-person-by-id", key = "#id")
    override fun addPhoto(id: String, photo: PhotoModel): Subscription =
        envolveInNewThread({
            persist(
                findById(id).let { fullPersonFound ->
                    fullPersonFound.copy(
                        complementary = fullPersonFound.complementary.copy(picture = photo.url),
                        photos = fullPersonFound.photos.toMutableList().apply { add(photo) }
                    )
                }
            )
        }, "addFullPersonPhoto")

    @CacheEvict("full-person-by-id", key = "#id")
    override fun removePhoto(id: String, photoId: String): Subscription =
        envolveInNewThread({
            persist(
                findById(id).let { fullPersonFound ->
                    fullPersonFound.copy(photos = fullPersonFound.photos.filter { it.id != photoId })
                }
            )
        }, "removeFullPersonPhoto")

    @CacheEvict("full-person-by-id", key = "#id")
    override fun updatePreferences(id: String, preferences: PreferencesModel): Subscription =
        envolveInNewThread({ persist(findById(id).copy(preferences = preferences)) }, "updateFullPersonPreferences")

    @CacheEvict("full-person-by-id", key = "#id")
    override fun updateLatestLocation(id: String, location: LocationModel): Subscription =
        envolveInNewThread({ persist(findById(id).copy(latestLocation = location)) }, "updateFullPersonLatestLocation")

    private fun envolveInNewThread(
        callable: Callable<out FullPersonModel>,
        taskName: String = "updateFullPersonInformation"
    ): Subscription =
        with(Schedulers.newThread()) {
            Observable.fromCallable(callable)
                .subscribeOn(this)
                .observeOn(this)
        }.subscribe(RxBgObserver(taskName))

    private fun persist(fullPerson: FullPersonModel): FullPersonModel =
        FullPersonModelMapper.inverseMap(
            repository.save(
                FullPersonModelMapper.map(fullPerson)
            )
        ).also { awsSqsClient.sendMessage(AwsQueue.PERSON_UPDATE.awsName, it.asSqsMessage()) }

}