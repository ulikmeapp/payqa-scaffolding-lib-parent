package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.LocationFacade
import com.ulikme.person.domain.models.LocationModel
import com.ulikme.person.domain.rr.request.LocationRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/person/{id}/location")
open class LocationController(
    private val locationFacade: LocationFacade
) {

    @ApiOperation(value = "Register a new location for specific person", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Location created")
    )
    @PostMapping
    fun register(
        @PathVariable id: String,
        @RequestBody request: LocationRequest
    ): ResponseEntity<LocationModel> = ResponseEntity.ok(locationFacade.register(id, request))

}