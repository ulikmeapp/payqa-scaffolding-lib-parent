package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.PhotoFacade
import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.service.PhotoService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/person/{id}/photo")
open class PhotoController(
    private val photoService: PhotoService,
    private val photoFacade: PhotoFacade
) {

    @ApiOperation(value = "Retrieve a photo's list of specific person", nickname = "listByPerson")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Photos retrieved")
    )
    @GetMapping
    fun listByPerson(
        @PathVariable id: String,
        @RequestParam(required = false) verification: Boolean = false
    ): ResponseEntity<List<PhotoModel>> = ResponseEntity.ok(photoService.listByPerson(id, verification))

    @ApiOperation(value = "Retrieve a specific photo of specific person", nickname = "findByName")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 200, message = "Photo retrieved")
    )
    @GetMapping("/{name}")
    fun findByName(
        @PathVariable id: String,
        @PathVariable name: String
    ): ResponseEntity<ByteArray> =
        ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(photoFacade.findByName(id, name))

    @ApiOperation(value = "Register a new photo for specific person", nickname = "register")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Photo created")
    )
    @PostMapping
    fun register(
        @PathVariable id: String,
        @RequestParam file: MultipartFile,
        request: HttpServletRequest
    ): ResponseEntity<PhotoModel> = ResponseEntity.ok(
        photoFacade.register(id, file, request)
    )

    @ApiOperation(value = "Delete a photo of specific person", nickname = "delete")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Photo not found"),
        ApiResponse(code = 204, message = "Photo deleted")
    )
    @DeleteMapping("/{name}")
    fun deleteByName(
        @PathVariable id: String,
        @PathVariable name: String
    ): ResponseEntity<Void> {
        photoFacade.delete(id, name)
        return ResponseEntity.noContent().build()
    }

}