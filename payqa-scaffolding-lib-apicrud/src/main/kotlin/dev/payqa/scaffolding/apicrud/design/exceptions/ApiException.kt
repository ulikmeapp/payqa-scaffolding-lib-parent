package dev.payqa.scaffolding.apicrud.design.exceptions

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class ApiException(
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val detail: ExceptionDetail,
    throwable: Throwable? = null
) : RuntimeException(detail.message, throwable) {

    constructor (message: String) : this(detail = ExceptionDetail(message = message))

    constructor(message: String, key: String) : this(detail = ExceptionDetail(message = message, key = key))

}