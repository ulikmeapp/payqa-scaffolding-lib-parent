package com.ulikme.person.api.rest

import com.ulikme.person.api.facade.VerificationFacade
import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.domain.rr.response.VerifyResponse
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/person/{id}")
open class VerificationController(
    private val verificationFacade: VerificationFacade
) {

    @ApiOperation(value = "Verify mobile phone", nickname = "verifyData")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 200, message = "Verifier code send")
    )
    @PostMapping("/verify")
    fun verifyData(
        @PathVariable id: String,
        @RequestParam(required = false) mobilePhone: String?
    ): ResponseEntity<VerifyResponse> = ResponseEntity.ok(
        verificationFacade.verifyData(id, mobilePhone)
    )

    @PostMapping("/verify/profile")
    fun verifyProfile(
        @PathVariable id: String,
        @RequestParam photo: MultipartFile,
        request: HttpServletRequest
    ): ResponseEntity<PhotoModel> = ResponseEntity.ok(
        verificationFacade.verifyProfile(id, photo, request)
    )

    @ApiOperation(value = "Validate mobile phone", nickname = "validateData")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 202, message = "Data validated")
    )
    @PatchMapping("/validate")
    fun validateData(
        @PathVariable id: String,
        @RequestParam verificationCode: String
    ): ResponseEntity<Void> {
        verificationFacade.validateData(id, verificationCode)
        return ResponseEntity.accepted().build()
    }

    @ApiOperation(value = "Validate profile", nickname = "validateProfile")
    @ApiResponses(
        ApiResponse(code = 500, message = "Internal server error"),
        ApiResponse(code = 404, message = "Person not found"),
        ApiResponse(code = 400, message = "Bad request"),
        ApiResponse(code = 202, message = "Validation accepted")
    )
    @PatchMapping("/validate/profile")
    fun validateProfile(
        @PathVariable id: String,
        @RequestParam valid: Boolean
    ): ResponseEntity<Void> {
        verificationFacade.validateProfile(id, valid)
        return ResponseEntity.accepted().build()
    }

}