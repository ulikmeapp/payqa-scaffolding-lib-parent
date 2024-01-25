package com.ulikme.utils.rx

import org.slf4j.LoggerFactory
import rx.Observer

open class RxBgObserver<T>(
    private val taskName: String
) : Observer<T> {

    companion object {
        private val log = LoggerFactory.getLogger(RxBgObserver::class.java)
    }

    override fun onCompleted() {
        log.info("Task [$taskName] completed in background.")
    }

    override fun onError(exception: Throwable?) {
        log.error("Task [$taskName] has returned an error: ${exception?.message}", exception)
    }

    override fun onNext(result: T) {
        log.debug("Task [$taskName] has returned a result: $result")
    }
}