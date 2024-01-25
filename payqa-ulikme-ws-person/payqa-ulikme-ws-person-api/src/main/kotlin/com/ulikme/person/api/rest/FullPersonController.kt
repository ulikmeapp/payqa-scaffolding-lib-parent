package com.ulikme.person.api.rest

import com.ulikme.person.domain.models.FullPersonModel
import com.ulikme.person.domain.rr.request.SearchRequest
import com.ulikme.person.service.FullPersonService
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.design.models.Data
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/person")
open class FullPersonController(
    private val fullPersonService: FullPersonService
) {

    @ApiOperation(value = "Paginate persons with their full information", nickname = "paginate")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Persons retrieved")
    )
    @GetMapping
    fun paginate(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<FullPersonModel>> =
        ResponseEntity.ok(
            fullPersonService.paginate(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder
                )
            )
        )

    @ApiOperation(value = "Main search", nickname = "search")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Persons retrieved")
    )
    @GetMapping("/search")
    fun paginate(
            @RequestParam pageNumber: Int,
            @RequestParam pageSize: Int,
            @RequestParam(required = false) sortOrder: String? = null,
            @RequestParam(required = false) sortField: String? = null,
            @ApiParam(value = "latitude") @RequestParam(name = "lat") latitude: BigDecimal? = null,
            @ApiParam(value = "longitude") @RequestParam(name = "lng") longitude: BigDecimal? = null,
            @ApiParam(value = "country") @RequestParam(required = false, name = "ctr") country: String? = null,
            @ApiParam(value = "distancePreference") @RequestParam(required = false, name = "dst") distance: Int? = null,
            @ApiParam(value = "showMe") @RequestParam(required = false, name = "sm") showMe: String? = null,
            @ApiParam(value = "agePreference") @RequestParam(required = false, name = "age") agePreference: Int? = null,
            @ApiParam(value = "minAgePreference") @RequestParam(required = false, name = "minAge") minAgePreference: Int? = null

    ): ResponseEntity<Page<FullPersonModel>> =

            ResponseEntity.ok(
                    mutableMapOf<String, Any>().let { params ->
                        // Preferences params
                        latitude?.let { params[SearchRequest::lat.name] = it }
                        longitude?.let { params[SearchRequest::lng.name] = it }
                        distance?.let {
                            val updatedDistance = it.times(2).toInt()
                            params[SearchRequest::dst.name] = updatedDistance
                        }
                        showMe?.let { params[SearchRequest::sm.name] = it }
                        agePreference?.let { params[SearchRequest::age.name] = it }
                        minAgePreference?.let { params[SearchRequest::minAge.name]=it}
                        country?.let { params[SearchRequest::ctr.name] = it }
                        // Current user param
                        params[SearchRequest::usr.name] = SecurityContext.getUser().id!!
                        // Do search
                        fullPersonService.paginate(
                                PaginateRequest(
                                        pageNumber,
                                        pageSize,
                                        sortColumn = sortField,
                                        sortDirection = sortOrder,
                                        params = params
                                )
                        )
                    }
            )

    @ApiOperation(value = "Main search for specific people", nickname = "listOnly")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Persons retrieved")
    )
    @GetMapping("/search-only")
    fun listOnly(
        @RequestParam id: Array<String>,
        @ApiParam(value = "latitude") @RequestParam(name = "lat") latitude: BigDecimal? = null,
        @ApiParam(value = "longitude") @RequestParam(name = "lng") longitude: BigDecimal? = null,
        @ApiParam(value = "country") @RequestParam(required = false, name = "ctr") country: String? = null,
        @ApiParam(value = "distancePreference") @RequestParam(required = false, name = "dst") distance: Int? = null,
        @ApiParam(value = "showMe") @RequestParam(required = false, name = "sm") showMe: String? = null,
        @ApiParam(value = "agePreference") @RequestParam(required = false, name = "age") agePreference: Int? = null,
        @ApiParam(value = "minAgePreference") @RequestParam(required = false, name = "minAge") minAgePreference: Int? = null

    ): ResponseEntity<Data<FullPersonModel>> =
        ResponseEntity.ok(
            fullPersonService.listOnly(
                SearchRequest(
                    lat = latitude,
                    lng = longitude,
                    ctr = country,
                    dst = distance,
                    sm = showMe,
                    age = agePreference,
                    minAge = minAgePreference,
                    usr = SecurityContext.getUser().id!!
                ),
                *id
            )
        )

    @GetMapping("/{id}/full")
    fun findById(@PathVariable id: String): ResponseEntity<FullPersonModel> =
        ResponseEntity.ok(fullPersonService.findById(id))

}