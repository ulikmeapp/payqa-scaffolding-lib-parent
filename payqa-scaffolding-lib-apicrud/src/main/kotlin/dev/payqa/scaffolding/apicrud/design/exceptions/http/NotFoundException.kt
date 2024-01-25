package dev.payqa.scaffolding.apicrud.design.exceptions.http

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus

class NotFoundException(
    message: String?
) : ApiException(
    httpStatus = HttpStatus.NOT_FOUND,
    detail = ExceptionDetail(
        message = message ?: "Not found"
    )
)