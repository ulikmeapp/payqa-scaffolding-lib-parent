package dev.payqa.scaffolding.apicrud.design.exceptions.http

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus

class InternalServerErrorException(
    message: String?,
    throwable: Throwable? = null
) : ApiException(
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    detail = ExceptionDetail(
        message = message ?: "Internal server error"
    ),
    throwable = throwable
)