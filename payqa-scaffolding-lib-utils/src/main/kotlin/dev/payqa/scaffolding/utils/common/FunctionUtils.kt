package dev.payqa.scaffolding.utils.common

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object FunctionUtils {

    @JvmStatic
    fun format(format: String, vararg values: Any): String {
        var output = format
        values.forEachIndexed { index, text ->
            output = output.replace("{$index}", text.toString())
        }
        return output
    }

    @JvmStatic
    fun toJson(value: Any): String = Gson().toJson(value)

    @JvmStatic
    fun <T> fromJson(json: String, clazz: Class<T>): T? =
        try {
            Gson().fromJson(json, clazz)
        } catch (exception: JsonSyntaxException) {
            null
        }

    @JvmStatic
    fun areTrue(vararg checks: Boolean): Boolean = checks.firstOrNull { !it } == null

    @JvmStatic
    fun areFalse(vararg checks: Boolean): Boolean = checks.firstOrNull { it } == null

    @JvmStatic
    fun <T> areIn(value: T, vararg checks: T): Boolean = checks.firstOrNull { it == value } != null

    @JvmStatic
    fun <T> areNotIn(value: T, vararg checks: T): Boolean = checks.firstOrNull { it == value } == null

    @JvmStatic
    fun <T> areIn(value: T, checks: List<T>): Boolean = checks.firstOrNull { it == value } != null

    @JvmStatic
    fun <T> areNotIn(value: T, checks: List<T>): Boolean = checks.firstOrNull { it == value } == null

}