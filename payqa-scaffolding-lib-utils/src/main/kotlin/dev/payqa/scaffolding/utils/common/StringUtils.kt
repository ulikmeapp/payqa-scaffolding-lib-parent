package dev.payqa.scaffolding.utils.common

import dev.payqa.scaffolding.utils.common.enums.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object StringUtils {

    @JvmStatic
    fun toDate(dateString: String): Date? = toDate(dateString, DateFormat.TIMESTAMP)

    @JvmStatic
    fun toDate(dateString: String, format: DateFormat = DateFormat.DEFAULT): Date? =
            try {
                SimpleDateFormat(format.value).parse(dateString)
            } catch (exception: ParseException) {
                null
            }

}