package dev.payqa.scaffolding.apicrud.design.exceptions

import org.springframework.http.HttpStatus

open class SoftlyException(
    val key: String? = null,
    override val message: String
) : ApiException(HttpStatus.OK, ExceptionDetail(key = key, message = message))