package com.ulikme.person.service

import com.ulikme.person.domain.models.*
import com.ulikme.person.domain.rr.request.SearchRequest
import dev.payqa.scaffolding.apicrud.design.models.Data
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import rx.Subscription

interface FullPersonService {

    fun paginate(request: PaginateRequest<FullPersonModel>): Page<FullPersonModel>

    fun listOnly(request: SearchRequest, vararg includedIds: String): Data<FullPersonModel>

    fun findById(id: String): FullPersonModel

    fun findByIdOrNull(id: String): FullPersonModel?

    fun save(fullPerson: FullPersonModel): Subscription

    fun save(
        person: PersonModel,
        photos: List<PhotoModel>? = null,
        preferences: PreferencesModel? = null,
        latestLocation: LocationModel? = null
    ): Subscription

    fun addPhoto(id: String, photo: PhotoModel): Subscription

    fun removePhoto(id: String, photoId: String): Subscription

    fun updatePreferences(id: String, preferences: PreferencesModel): Subscription

    fun updateLatestLocation(id: String, location: LocationModel): Subscription

}