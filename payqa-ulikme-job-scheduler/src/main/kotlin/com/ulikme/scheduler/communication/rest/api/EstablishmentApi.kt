package com.ulikme.scheduler.communication.rest.api

import com.ulikme.establishment.domain.models.EstablishmentModel
import dev.payqa.scaffolding.apicrud.design.models.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

const val ESTABLISHMENT_BASE = "api/v1/establishment"
const val ESTABLISHMENT_PAGINATE_BY_LOCATION = "$ESTABLISHMENT_BASE/location"

interface EstablishmentApi {

    @GET(ESTABLISHMENT_PAGINATE_BY_LOCATION)
    fun paginateByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("sortField") sortField: String? = null
    ): Observable<Response<Page<EstablishmentModel>>>

}