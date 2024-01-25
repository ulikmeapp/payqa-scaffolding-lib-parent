package dev.payqa.scaffolding.apicrud.design.exceptions.http

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus

class BadRequestException(
    message: String?,
    throwable: Throwable? = null
) : ApiException(
    httpStatus = HttpStatus.BAD_REQUEST,
    detail = ExceptionDetail(
        message = message ?: "Bad request"
    ),
    throwable = throwable
)