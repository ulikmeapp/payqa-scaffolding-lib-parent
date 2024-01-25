package dev.payqa.scaffolding.apicrud.design.exceptions

class CommunicationException(
    url: String,
    override val message: String,
    throwable: Throwable? = null
) : ApiException(
    detail = ExceptionDetail(
        message = "$message - on $url"
    ),
    throwable = throwable
)