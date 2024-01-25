package com.ulikme.utils.http.support

import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException
import org.springframework.web.servlet.support.RequestContextUtils
import java.util.TimeZone
import javax.servlet.http.HttpServletRequest

open class RequestContext
private constructor(
    val request: HttpServletRequest
) {

    companion object {
        private var instance: RequestContext? = null

        fun init(request: HttpServletRequest) {
            if (instance == null) {
                instance = RequestContext(request)
            }
        }

        fun getTimezone(): TimeZone = instance?.request?.let { RequestContextUtils.getTimeZone(it)!! }
            ?: throw InternalServerErrorException(NOT_INITIALIZED_CONTEXT)
    }

}