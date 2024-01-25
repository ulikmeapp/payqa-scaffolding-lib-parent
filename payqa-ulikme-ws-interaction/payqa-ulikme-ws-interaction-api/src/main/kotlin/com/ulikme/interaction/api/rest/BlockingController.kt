package com.ulikme.interaction.api.rest

import com.ulikme.interaction.api.facade.BlockingFacade
import com.ulikme.interaction.domain.models.BlockingModel
import com.ulikme.interaction.service.BlockingService
import com.ulikme.utils.http.support.SecurityContext
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/blocking")
open class BlockingController(
    private val blockingService: BlockingService,
    private val blockingFacade: BlockingFacade
) {

    @GetMapping("/user/{id}")
    fun paginateByPerson(
        @PathVariable id: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<BlockingModel>> =
        ResponseEntity.ok(
            blockingService.paginateByPerson(
                id,
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder
                )
            )
        )

    @PostMapping
    fun block(
        @RequestParam blockedPersonId: String
    ): ResponseEntity<BlockingModel> =
        ResponseEntity.ok(blockingFacade.block(blockedPersonId))

    @DeleteMapping
    fun unBlock(
        @RequestParam blockedPersonId: String
    ): ResponseEntity<Void> {
        blockingService.unBlock(SecurityContext.getUser().id!!, blockedPersonId)
        return ResponseEntity.noContent().build()
    }


}