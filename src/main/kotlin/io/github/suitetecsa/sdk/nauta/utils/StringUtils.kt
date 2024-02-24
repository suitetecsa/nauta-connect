package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val SECONDS_PER_MINUTE = 60
private const val KILOBYTE = 1024.0
private const val MEGABYTE = KILOBYTE * KILOBYTE
private const val GIGABYTE = MEGABYTE * KILOBYTE

object StringUtils {
    @JvmStatic
    @Throws(InvalidSessionException::class)
    fun toSeconds(time: String): Long {
        if (time == "errorop") {
            throw InvalidSessionException("La sesión ya no es válida")
        }

        val parts = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var totalSeconds: Long = 0

        for (part in parts) {
            totalSeconds = totalSeconds * SECONDS_PER_MINUTE + part.toLong()
        }

        return totalSeconds
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun toDateMillis(date: String): Long {
        val calendar = Calendar.getInstance()
        calendar.time = toDate(date)
        return calendar.timeInMillis
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun toDate(date: String): Date {
        return SimpleDateFormat("dd/MM/yy", Locale.getDefault()).parse(date)
    }

    private fun convertToBytes(sizeValue: String, sizeUnit: String): Long {
        val toNumber = sizeValue.replace(",", ".").toDouble()

        val multi = toNumber * when (sizeUnit) {
            "KB" -> KILOBYTE
            "MB" -> MEGABYTE
            "GB" -> GIGABYTE
            else -> throw IllegalArgumentException("La unidad de tamaño no es válida")
        }

        return multi.toLong()
    }

    @JvmStatic
    fun toBytes(bytes: String): Long {
        val splitText = bytes.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sizeUnit = splitText[splitText.size - 1]
        val sizeValue = bytes.replace(" $sizeUnit", "").replace(" ", "")
        return convertToBytes(sizeValue, sizeUnit.uppercase())
    }

    /**
     * Parses a price string like "$1,000.99 CUP" to a [Double] value.
     *
     * @param price The input price string to parse.
     * @return The parsed numeric value or null if unable to parse.
     */
    @JvmStatic
    fun fromPriceString(price: String): Double {
        if (!price.matches("\\$?\\d+[,.]?\\d* CUP?".toRegex())) {
            throw ParseException("", 0)
        }

        return price.replace("[$, CUP]".toRegex(), "").replace(",", ".").toDouble()
    }


}
