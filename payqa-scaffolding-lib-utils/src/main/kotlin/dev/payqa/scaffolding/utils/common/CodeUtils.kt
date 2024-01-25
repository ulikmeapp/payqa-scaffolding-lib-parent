package dev.payqa.scaffolding.utils.common

import kotlin.math.floor

object CodeUtils {

    fun generateCode(): String = (floor(Math.random() * 999999) + 111111).toInt().toString()

}