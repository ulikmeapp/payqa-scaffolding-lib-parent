package com.ulikme.utils.http.support

import dev.payqa.scaffolding.apicrud.design.exceptions.http.InternalServerErrorException

open class BackendContext
private constructor(
    val token: String
) {

    companion object {
        private var instance: BackendContext? = null

        fun init(token: String) {
            if (instance == null || instance!!.token != token)
                instance = BackendContext(token)
        }

        fun getToken(): String = instance?.token
            ?: throw InternalServerErrorException(NOT_INITIALIZED_CONTEXT)
    }

}