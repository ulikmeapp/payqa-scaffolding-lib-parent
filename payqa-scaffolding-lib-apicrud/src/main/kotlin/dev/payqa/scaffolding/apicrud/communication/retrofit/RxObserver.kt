package dev.payqa.scaffolding.apicrud.communication.retrofit

import dev.payqa.scaffolding.apicrud.design.rest.rr.response.ErrorResponse
import dev.payqa.scaffolding.utils.common.FunctionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import retrofit2.Response
import rx.Observer

@Suppress("UnusedLambdaExpressionBody")
class RxObserver<K>(
    private val callback: RetrofitCallback<K>
) : Observer<Response<K>> {

    companion object {
        private val log = LoggerFactory.getLogger(RxObserver::class.java.simpleName)
    }

    override fun onCompleted() = log.debug("Observation completed")

    override fun onError(throwable: Throwable) {
        log.error("[onError]", throwable)
        this.callback.onFailed(throwable.message!!)
    }

    override fun onNext(response: Response<K>) {
        if (response.isSuccessful) {
            when (response.code()) {
                HttpStatus.OK.value() -> this.callback.onSuccess(response.body()!!)
                HttpStatus.ACCEPTED.value() -> this.callback.onAccepted()
                HttpStatus.NO_CONTENT.value() -> this.callback.onNoContent()
            }
        } else {
            val errorResponse = FunctionUtils.fromJson(
                String(response.errorBody()!!.bytes()),
                ErrorResponse::class.java
            )

            log.error("[onNext] error into ${this::class.java.simpleName} for ${response.raw().request().url()}")
            log.error("[onNext] error description: ${errorResponse?.message}")

            if (response.code() in HttpStatus.BAD_REQUEST.value()..HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value() &&
                    errorResponse != null) {
                this.callback.onInvalid(errorResponse.code, errorResponse.message!!);
            } else {
                this.callback.onFailed("Error on communication with ${response.raw().request().url()}")
            }
        }
    }

}