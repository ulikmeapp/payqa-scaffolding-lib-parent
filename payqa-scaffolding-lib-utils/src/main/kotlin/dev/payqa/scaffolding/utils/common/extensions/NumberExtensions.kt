package dev.payqa.scaffolding.utils.common.extensions

import dev.payqa.scaffolding.utils.common.NumberUtils
import java.math.BigDecimal

fun BigDecimal.toFixed(decimals: Int) = NumberUtils.formatToFixed(bigDecimal = this, decimals = decimals)

fun Double.toFixed(decimals: Int) = NumberUtils.formatToFixed(double = this, decimals = decimals)