package com.ulikme.person.api.facade

import com.ulikme.person.domain.models.PhotoModel
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

interface PhotoFacade {

    fun findByName(id: String, name: String): ByteArray

    fun register(
        id: String,
        file: MultipartFile,
        request: HttpServletRequest
    ): PhotoModel

    fun delete(id: String, name: String)

}