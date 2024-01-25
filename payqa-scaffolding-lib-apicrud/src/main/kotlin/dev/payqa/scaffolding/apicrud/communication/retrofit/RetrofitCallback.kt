package dev.payqa.scaffolding.apicrud.communication.retrofit

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException

interface RetrofitCallback<K> {

    // 200
    fun onSuccess(response: K): Unit = throw ApiException("onSuccess method has not been implemented.")

    // 202
    fun onAccepted(): Unit = throw ApiException("onAccepted method has not been implemented.")

    // 204
    fun onNoContent(): Unit = throw ApiException("onNoContent method has not been implemented.")

    // 400 to 451
    fun onInvalid(code: Int, message: String): Unit = throw ApiException("onInvalid method has not been implemented.")

    fun onFailed(message: String)

}