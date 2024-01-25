package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.PersonFacade
import com.ulikme.person.domain.models.PreferencesModel
import com.ulikme.person.domain.rr.request.PreferencesRequest
import com.ulikme.person.service.PreferencesService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/person/{id}/preferences")
open class PreferencesController(
    private val preferencesService: PreferencesService,
    private val personFacade: PersonFacade
) {

    @ApiOperation(value = "Get preferences of specific person", nickname = "find")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Preferences not found"),
        ApiResponse(code = 200, message = "Preferences found")
    )
    @GetMapping
    fun find(@PathVariable id: String): ResponseEntity<PreferencesModel> =
        ResponseEntity.ok(preferencesService.findByPersonId(id))

    @ApiOperation(value = "Update preferences of specific person", nickname = "update")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Preferences not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Preferences updated")
    )
    @PatchMapping
    fun update(
        @PathVariable id: String,
        @RequestBody request: PreferencesRequest
    ): ResponseEntity<PreferencesModel> = ResponseEntity.ok(personFacade.updatePreferences(id, request))

}