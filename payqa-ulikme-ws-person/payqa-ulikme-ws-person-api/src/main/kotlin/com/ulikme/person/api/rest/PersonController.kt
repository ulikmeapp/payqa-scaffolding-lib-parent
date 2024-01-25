package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.PersonFacade
import com.ulikme.person.domain.models.PersonModel
import com.ulikme.person.domain.rr.request.PersonRequest
import com.ulikme.person.service.PersonService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/person")
open class PersonController(
    private val personService: PersonService,
    private val personFacade: PersonFacade
) {

    @ApiOperation(value = "Find person by id", nickname = "findById")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 200, message = "Person found")
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<PersonModel> =
        ResponseEntity.ok(personService.findById(id))

    @ApiOperation(value = "Find person by email", nickname = "findByEmail")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 200, message = "Person found")
    )
    @GetMapping("/email/{email}")
    fun findByEmail(@PathVariable email: String): ResponseEntity<PersonModel> =
        ResponseEntity.ok(personService.findByEmail(email))

    @ApiOperation(value = "Register a new person", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Person created")
    )
    @PostMapping
    fun register(@RequestBody request: PersonRequest): ResponseEntity<PersonModel> =
        ResponseEntity.ok(personFacade.register(request)).also {
            println("Hello, World!")
        }

    @ApiOperation(value = "Update person information", nickname = "update")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Person updated")
    )
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: PersonRequest
    ): ResponseEntity<PersonModel> = ResponseEntity.ok(personFacade.update(id, request))

    @ApiOperation(value = "Delete a person by their id", nickname = "delete")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 204, message = "Person deleted")
    )
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String
    ): ResponseEntity<Void> {
        personService.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}