package com.ulikme.person.api.facade

import com.ulikme.person.domain.models.PhotoModel
import com.ulikme.person.domain.rr.response.VerifyResponse
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

interface VerificationFacade {

    fun verifyData(personId: String, mobilePhone: String? = null): VerifyResponse

    fun verifyProfile(personId: String, photo: MultipartFile, request: HttpServletRequest): PhotoModel

    fun validateData(personId: String, verificationCode: String)

    fun validateProfile(personId: String, valid: Boolean)

}