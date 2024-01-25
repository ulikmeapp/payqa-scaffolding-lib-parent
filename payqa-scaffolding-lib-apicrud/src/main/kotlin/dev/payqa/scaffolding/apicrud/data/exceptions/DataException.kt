package dev.payqa.scaffolding.apicrud.data.exceptions

import dev.payqa.scaffolding.apicrud.data.enums.DataErrorEnum
import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus

class DataException(
    errorType: DataErrorEnum?,
    val key: String? = null,
    override val message: String,
    throwable: Throwable? = null
) : ApiException(
    when (errorType) {
        DataErrorEnum.NOT_FOUND -> HttpStatus.NO_CONTENT
        DataErrorEnum.NON_EXISTENT -> HttpStatus.NOT_FOUND
        DataErrorEnum.INVALID_DATA -> HttpStatus.PRECONDITION_REQUIRED
        DataErrorEnum.INVALID_OPERATION -> HttpStatus.NOT_ACCEPTABLE
        else -> HttpStatus.INTERNAL_SERVER_ERROR
    },
    ExceptionDetail(
        key = key,
        message = message
    ),
    throwable
) {

    constructor(errorType: DataErrorEnum, message: String) : this(errorType, null, message, null)

    constructor(key: String, message: String) : this(null, key, message, null)

    constructor(message: String, throwable: Throwable) : this(null, null, message, throwable)

    constructor(message: String) : this(null, null, message, null)

    constructor(message: String, vararg args: Any) : this(null, null, String.format(message, args), null)

}