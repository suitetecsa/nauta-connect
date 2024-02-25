package io.github.suitetecsa.sdk.nauta.utils

import java.util.*

object DoubleUtils {
    @JvmStatic
    fun toPriceString(price: Double) = String.format(Locale.getDefault(), "$%.2f CUP", price)
}
