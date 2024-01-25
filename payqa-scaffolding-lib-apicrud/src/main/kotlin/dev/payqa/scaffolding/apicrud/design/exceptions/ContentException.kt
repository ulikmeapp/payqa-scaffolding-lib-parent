package dev.payqa.scaffolding.apicrud.design.exceptions

class ContentException(
    val key: String? = null,
    override val message: String,
    throwable: Throwable? = null
) : ApiException(
    detail = ExceptionDetail(
        key = key,
        message = message
    ),
    throwable = throwable
) {

    constructor(message: String) : this(null, message, null)

    constructor(message: String, throwable: Throwable) : this(null, message, throwable)

}