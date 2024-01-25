package com.ulikme.utils.common

import dev.payqa.scaffolding.apicrud.data.exceptions.ValidationException
import org.slf4j.LoggerFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

object DateTimeUtils {

    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

    private val log = LoggerFactory.getLogger(DateTimeUtils::class.java)

    fun parse(d0: String, format: String = DEFAULT_DATE_FORMAT): Date? =
        try {
            SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(d0)
        } catch (exception: ParseException) {
            log.error("Invalid entered date $d0: ${exception.message}", exception)
            null
        }

    fun convertToTimeZone(
        date: Date = Date(),
        z0: ZoneId = ZoneId.systemDefault(),
        z1: ZoneId = ZoneId.systemDefault()
    ): Date =
        Date.from(date.toInstant().atZone(z0).toLocalDateTime().atZone(z1).toInstant())

    fun startOfDay(date: Date = Date()): Date =
        Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
        }.time

    fun endOfDay(date: Date = Date()): Date =
        Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 24)
        }.time

    fun hoursUntilFinishDay(date: Date = Date()): Int =
        Calendar.getInstance().apply { time = date }
            .let { calendarDate -> 24 - calendarDate.get(Calendar.HOUR_OF_DAY) }

}