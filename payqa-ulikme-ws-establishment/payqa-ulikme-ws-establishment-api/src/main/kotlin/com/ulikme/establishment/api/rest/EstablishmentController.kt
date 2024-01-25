package com.ulikme.establishment.api.rest

import com.ulikme.establishment.api.facade.EstablishmentFacade
import com.ulikme.establishment.domain.models.EstablishmentModel
import com.ulikme.establishment.domain.rr.request.EstablishmentRequest
import com.ulikme.establishment.service.EstablishmentService
import dev.payqa.scaffolding.apicrud.design.models.Page
import dev.payqa.scaffolding.apicrud.design.rest.rr.request.PaginateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/establishment")
open class EstablishmentController(
    private val establishmentService: EstablishmentService,
    private val establishmentFacade: EstablishmentFacade
) {

    @GetMapping
    fun paginate(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<EstablishmentModel>> =
        ResponseEntity.ok(
            establishmentService.paginate(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder
                )
            )
        )

    @GetMapping("/country/{code}")
    fun paginateByCountry(
        @PathVariable code: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<EstablishmentModel>> =
        ResponseEntity.ok(
            establishmentService.paginateByCountry(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "countryCode" to code
                    )
                )
            )
        )

    @GetMapping("/location")
    fun paginateByLocation(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @RequestParam(required = false) sortOrder: String? = null,
        @RequestParam(required = false) sortField: String? = null
    ): ResponseEntity<Page<EstablishmentModel>> =
        ResponseEntity.ok(
            /*establishmentService.paginateByLocation(
                PaginateRequest(
                    pageNumber,
                    pageSize,
                    sortColumn = sortField,
                    sortDirection = sortOrder,
                    params = mapOf(
                        "latitude" to latitude,
                        "longitude" to longitude
                    )
                )*/
                establishmentService.paginate(
                        PaginateRequest(
                                pageNumber,
                                pageSize,
                                sortColumn = sortField,
                                sortDirection = sortOrder
                        )
                )
            
        )

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<EstablishmentModel> =
        ResponseEntity.ok(establishmentService.findById(id))

    @PostMapping
    fun register(@RequestBody request: EstablishmentRequest): ResponseEntity<EstablishmentModel> =
        ResponseEntity.ok(establishmentFacade.register(request))

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: EstablishmentRequest
    ): ResponseEntity<EstablishmentModel> =
        ResponseEntity.ok(establishmentFacade.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        establishmentService.delete(id)
        return ResponseEntity.noContent().build()
    }

}