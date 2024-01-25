package dev.payqa.scaffolding.utils.common.enums

enum class DateFormat(val value: String, val regex: String) {

    DEFAULT("dd-MM-yyyy HH:mm:ss", ""),
    TIMESTAMP("dd-MM-yyyy HH:mm:ss", ""),
    ENGLISH_TIMESTAMP("yyyy-MM-dd HH:mm:ss", ""),
    DATE("dd-MM-yyyy", "([0-9]{1,2}(-)[0-9]{1,2}(-)[0-9]{4})"),
    ENGLISH_DATE("yyyy-MM-dd", ""),
    TIME("HH:mm:ss", ""),
    SHORT_TIME("HH:mm", "")

}