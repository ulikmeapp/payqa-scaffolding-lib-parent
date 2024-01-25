package dev.payqa.scaffolding.utils.common

import java.math.BigDecimal
import java.text.DecimalFormat

object NumberUtils {

    @JvmStatic
    fun formatToFixed(
        bigDecimal: BigDecimal? = null,
        double: Double? = null,
        decimals: Int
    ): String = DecimalFormat(
        "#0.${String.format("%0${decimals}d", 0)}"
    ).format(bigDecimal ?: double)

}