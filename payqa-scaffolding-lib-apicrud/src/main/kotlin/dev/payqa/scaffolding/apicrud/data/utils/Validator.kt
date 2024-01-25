package dev.payqa.scaffolding.apicrud.data.utils

import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import java.util.*

object Validator {

    fun checkNotEmpty(value: String?, fieldName: String, key: String? = null) =
        value?.let {
            when (it.isEmpty()) {
                true -> throw ValidationException(key, "[$fieldName] cannot be null or empty")
                false -> Unit
            }
        }

    fun checkNotNull(value: Any?, fieldName: String, key: String? = null) =
        when (Objects.nonNull(value)) {
            false -> throw ValidationException(key, "[$fieldName] cannot be null")
            true -> Unit
        }

    fun checkNotExceedSize(value: String?, max: Int, fieldName: String, key: String? = null) =
        value?.let {
            when (it.isNotEmpty() && it.length > max) {
                true -> throw ValidationException(key, "[$fieldName] cannot exceed $max characters")
                false -> Unit
            }
        }

    fun checkGreaterThan(value: String?, min: Int, fieldName: String, key: String? = null) =
        value?.let {
            when (it.length < min) {
                true -> throw ValidationException(key, "[$fieldName] cannot be greater than $min characters")
                false -> Unit
            }
        }

    fun checkSmallerThan(value: Int?, max: Int, fieldName: String, key: String? = null) =
        value?.let {
            when (it > max) {
                true -> throw ValidationException(key, "[$fieldName] must be smaller than $max")
                false -> Unit
            }
        }

    fun checkTrue(check: Boolean, message: String?, key: String? = null): Unit =
        when (check) {
            false -> throw ValidationException(key, message ?: "Conflict with check validation")
            true -> Unit
        }

    fun checkFalse(check: Boolean, message: String?, key: String? = null): Unit =
        when (check) {
            true -> throw ValidationException(key, message ?: "Conflict with check validation")
            false -> Unit
        }

}