package dev.payqa.scaffolding.utils.common

import dev.payqa.scaffolding.utils.common.enums.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val CALENDAR = Calendar.getInstance()

    @JvmStatic
    fun getCurrent(field: Int): Int = CALENDAR.get(field)

    @JvmStatic
    fun getCurrentYear(): Int = CALENDAR.get(Calendar.YEAR)

    @JvmStatic
    fun getCurrentDayOfMonth(): Int = CALENDAR.get(Calendar.DAY_OF_MONTH)

    @JvmStatic
    fun getCurrentDayOfWeek(): Int = CALENDAR.get(Calendar.DAY_OF_WEEK)

    @JvmStatic
    fun get(date: Date, field: Int): Int {
        val instance = Calendar.getInstance()
        instance.time = date
        return instance.get(field)
    }

    @JvmStatic
    fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    @JvmStatic
    fun getDayOfMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    @JvmStatic
    fun getDayOfWeek(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    @JvmStatic
    fun getOnlyDate(date: Date): Date {
        val formatter = SimpleDateFormat(DateFormat.DATE.value)
        return formatter.parse(formatter.format(date))
    }

    @JvmStatic
    fun getOnlyTime(date: Date): Date {
        val formatter = SimpleDateFormat(DateFormat.TIME.value)
        return formatter.parse(formatter.format(date))
    }

    @JvmStatic
    fun createFrom(field: Int, value: Int): Date {
        val calendar = Calendar.getInstance()
        if (!FunctionUtils.areIn(
                field,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE,
                Calendar.SECOND
            )
        )
            throw UnsupportedOperationException("Field must be in YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE or SECOND")
        calendar.set(
                if (field == Calendar.YEAR) value else 0,
                if (field == Calendar.MONTH) value else 0,
                if (field == Calendar.DAY_OF_MONTH) value else 0,
                if (field == Calendar.HOUR_OF_DAY) value else 0,
                if (field == Calendar.MINUTE) value else 0,
                if (field == Calendar.SECOND) value else 0
        )
        return calendar.time
    }

    @JvmStatic
    fun format(date: Date): String = format(date, DateFormat.TIMESTAMP)

    @JvmStatic
    fun format(date: Date, format: DateFormat = DateFormat.DEFAULT) =
            SimpleDateFormat(format.value).format(date)

    @JvmStatic
    fun parse(date: String, format: DateFormat = DateFormat.DEFAULT) =
            SimpleDateFormat(format.value).parse(date)

}