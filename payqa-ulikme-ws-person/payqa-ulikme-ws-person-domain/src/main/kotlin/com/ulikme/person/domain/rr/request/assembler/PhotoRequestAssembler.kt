package com.ulikme.person.domain.rr.request.assembler

import com.ulikme.person.domain.models.PhotoModel
import dev.payqa.scaffolding.apicrud.communication.aws.client.S3Key
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

object PhotoRequestAssembler {

    fun toModel(personId: String, photo: MultipartFile, request: HttpServletRequest, s3Key: S3Key): PhotoModel =
        PhotoModel(
            personId = personId,
            key = s3Key.build(),
            filename = photo.originalFilename ?: photo.name
        ).copy(
            url = buildPictureUrl(request, personId, photo.originalFilename ?: photo.name)
        )

    fun buildPictureUrl(request: HttpServletRequest, personId: String, name: String) =
        "http://${request.serverName}:${request.serverPort}/api/v1/person/$personId/photo/$name"

}