package dev.payqa.scaffolding.apicrud.design.exceptions

import dev.payqa.scaffolding.apicrud.design.enums.AuthorizationTypeEnum
import org.springframework.http.HttpStatus

class UnauthorizedException(
    authorizationType: AuthorizationTypeEnum,
    val key: String? = null,
    override val message: String,
    throwable: Throwable? = null
) : ApiException(
    when (authorizationType) {
        AuthorizationTypeEnum.FOR_PAYMENT -> HttpStatus.PAYMENT_REQUIRED
        AuthorizationTypeEnum.FOR_PERMISSION -> HttpStatus.UNAUTHORIZED
    },
    ExceptionDetail(
        key = key,
        message = message
    ),
    throwable
) {

    constructor(
        authorizationType: AuthorizationTypeEnum,
        message: String
    ) : this(authorizationType, null, message, null)

}