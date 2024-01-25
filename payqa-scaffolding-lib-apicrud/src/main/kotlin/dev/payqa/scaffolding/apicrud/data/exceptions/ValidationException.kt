package dev.payqa.scaffolding.apicrud.data.exceptions

import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail
import org.springframework.http.HttpStatus
import org.springframework.util.StringUtils
import java.util.*

class ValidationException(
    val key: String? = null,
    override val message: String
) : ApiException(
    HttpStatus.BAD_REQUEST,
    ExceptionDetail(key = key, message = message)
) {

    constructor(message: String) : this(null, message)

    constructor(message: String, vararg args: Any) : this(String.format(message, args))

    companion object {

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkNotEmpty")
        )
        @JvmStatic
        fun checkNotEmpty(value: String?, fieldName: String, key: String? = null) =
            when {
                value?.isEmpty() ?: false ->
                    throw ValidationException(key, "[$fieldName] cannot be null or empty")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkNotNull")
        )
        @JvmStatic
        fun checkNotNull(value: Any?, fieldName: String, key: String? = null) =
            when {
                Objects.isNull(value) ->
                    throw ValidationException(key, "[$fieldName] cannot be null")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkNotExceedSize")
        )
        @JvmStatic
        fun checkNotExceedSize(value: String?, max: Int, fieldName: String, key: String? = null) =
            when {
                !StringUtils.isEmpty(value) && value!!.length > max ->
                    throw ValidationException(key, "[$fieldName] cannot exceed $max characters")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkGreaterThan")
        )
        @JvmStatic
        fun checkGreaterThan(value: String?, min: Int, fieldName: String, key: String? = null) =
            when {
                !StringUtils.isEmpty(value) && value!!.length < min ->
                    throw ValidationException(key, "[$fieldName] cannot be greater than $min characters")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkSmallerThan")
        )
        @JvmStatic
        fun checkSmallerThan(value: Int?, max: Int, fieldName: String, key: String? = null) =
            when {
                !Objects.isNull(value) && value!! > max ->
                    throw ValidationException(key, "[$fieldName] must be smaller than $max")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkTrue")
        )
        @JvmStatic
        fun checkTrue(check: Boolean, message: String?, key: String? = null): Unit =
            when (check) {
                false -> throw ValidationException(key, message ?: "Conflict with check validation")
                else -> Unit
            }

        @Deprecated(
            message = "Must use Validator function",
            replaceWith = ReplaceWith(expression = "Validator.checkFalse")
        )
        @JvmStatic
        fun checkFalse(check: Boolean, message: String?, key: String? = null): Unit =
            when (!check) {
                false -> throw ValidationException(key, message ?: "Conflict with check validation")
                else -> Unit
            }

    }

}