package com.ulikme.interaction.api.rest

import com.ulikme.interaction.api.config.PropertiesConfiguration
import com.ulikme.interaction.api.facade.InteractionFacade
import com.ulikme.interaction.domain.models.InteractionModel
import com.ulikme.interaction.domain.rr.request.InteractionRequest
import com.ulikme.interaction.service.InteractionService
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import dev.payqa.scaffolding.apicrud.design.rest.rr.response.ErrorResponse
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/interaction")
open class InteractionController(
    private val interactionService: InteractionService,
    private val interactionFacade: InteractionFacade,
    private val propertiesConfiguration: PropertiesConfiguration
) {

    @ApiOperation(
        value = "Paginate interactions by person who received interaction",
        nickname = "paginateByPersonInteracted"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Interactions retrieved")
    )
    @GetMapping("/user/{id}/received")
    fun paginateByPersonInteracted(
        @PathVariable id: String,
        @RequestParam(required = false) type: String? = null,
        @RequestParam(required = false) classification: String? = null,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<InteractionModel>> =
        ResponseEntity.ok(
            interactionService.paginateByPersonInteracted(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "personInteractedId" to id,
                        InteractionModel::type.name to (type ?: ""),
                        InteractionModel::classification.name to (classification ?: "")
                    )
                )
            )
        )

    @ApiOperation(
        value = "Paginate interactions by person who made interaction",
        nickname = "paginateByCreatedPerson"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Interactions retrieved")
    )
    @GetMapping("/user/{id}/made")
    fun paginateByCreatedPerson(
        @PathVariable id: String,
        @RequestParam(required = false) type: String? = null,
        @RequestParam(required = false) classification: String? = null,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<InteractionModel>> =
        ResponseEntity.ok(
            interactionService.paginateByCreatedPerson(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "createdPersonId" to id,
                        InteractionModel::type.name to (type ?: ""),
                        InteractionModel::classification.name to (classification ?: "")
                    )
                )
            )
        )

    @ApiOperation(value = "Paginate matches by specific person", nickname = "paginateMatchsByCreatedPerson")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Matches retrieved")
    )
    @GetMapping("/user/{id}/matches")
    fun paginateMatchesByCreatedPerson(
        @PathVariable id: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<InteractionModel>> =
        ResponseEntity.ok(
            interactionService.paginateMatchesByPerson(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "createdPersonId" to id,
                        InteractionModel::match.name to true
                    )
                )
            )
        )

    @ApiOperation(value = "Find specific interaction by their id", nickname = "findById")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Interaction not found"),
        ApiResponse(code = 200, message = "Interaction found")
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<InteractionModel> =
        ResponseEntity.ok(interactionService.findById(id))

    @ApiOperation(value = "Register a new interaction", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 409, message = "Same interaction already exists", response = ErrorResponse::class),
        ApiResponse(code = 402, message = "Payment required for more interactions"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Interaction created")
    )
    @PostMapping
    fun register(
        @RequestBody request: InteractionRequest
    ): ResponseEntity<InteractionModel> =
        ResponseEntity.ok(interactionFacade.register(request))

    @ApiOperation(value = "Delete specific interaction by their id", nickname = "deleteById")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Interaction not found"),
        ApiResponse(code = 204, message = "Interaction deleted")
    )
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<Void> {
        interactionService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @ApiOperation(
        value = "Delete all behavior interactions by specific classification or simply all",
        nickname = "deleteBehaviorByCreatedPerson"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 204, message = "Interactions deleted")
    )
    @DeleteMapping("/behavior/user/{id}")
    fun deleteBehaviorByCreatedPerson(
        @PathVariable id: String,
        @RequestParam(required = false) classification: String? = null
    ): ResponseEntity<Void> {
        interactionService.deleteBehaviorTypeByCreatedPerson(id, classification)
        return ResponseEntity.noContent().build()
    }

}