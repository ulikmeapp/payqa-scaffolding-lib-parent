package dev.payqa.scaffolding.apicrud.design.rest.handler

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.rest.rr.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorControllerAdvice {

    companion object {
        private val log = LoggerFactory.getLogger(ErrorControllerAdvice::class.java)
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<ErrorResponse> {
        log.error("Handling api exception to transform friendly message: [exception={}]", exception.message, exception);
        return ResponseEntity(
            ErrorResponse(
                exception.detail.code ?: exception.httpStatus.value(),
                key = exception.detail.key,
                message = exception.detail.message
            ), exception.httpStatus
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        log.error("Handling exception to transform friendly message: [exception={}]", exception.message, exception);
        return ResponseEntity(
            ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = exception.message
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

}