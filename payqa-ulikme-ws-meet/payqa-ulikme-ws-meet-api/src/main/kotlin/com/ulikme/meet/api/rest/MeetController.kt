package com.ulikme.meet.api.rest

import com.ulikme.meet.api.facade.MeetFacade
import com.ulikme.meet.domain.models.MeetModel
import com.ulikme.meet.domain.rr.request.MeetRequest
import com.ulikme.meet.service.MeetService
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meet")
open class MeetController(
    private val meetFacade: MeetFacade,
    private val meetService: MeetService
) {

    @ApiOperation(
        value = "Paginate meets by person who participate in them",
        nickname = "paginate"
    )
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Meets retrieved")
    )
    @GetMapping("/user/{id}")
    fun paginate(
        @PathVariable id: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<MeetModel>> =
        ResponseEntity.ok(
            meetService.paginate(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "assistantInformationId" to id,
                    )
                )
            )
        )

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<MeetModel> =
        ResponseEntity.ok(meetService.findById(id))

    @PostMapping
    fun register(
        @RequestBody request: MeetRequest
    ): ResponseEntity<MeetModel> =
        ResponseEntity.ok(meetFacade.register(request))

    @PatchMapping("/{id}/qualify/{qualification}")
    fun qualify(
        @PathVariable id: String,
        @PathVariable qualification: String
    ): ResponseEntity<MeetModel> =
        ResponseEntity.ok(meetFacade.qualify(id, qualification))

}