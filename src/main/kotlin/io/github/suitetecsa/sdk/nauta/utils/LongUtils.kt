package io.github.suitetecsa.sdk.nauta.utils

import java.text.SimpleDateFormat
import java.util.*

private const val SECONDS_IN_HOUR = 3600
private const val SECONDS_IN_MINUTE = 60

object LongUtils {
    @JvmStatic
    fun toDateString(date: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(calendar.time)
    }

    @JvmStatic
    fun toTimeString(time: Long): String {
        val hours = time / SECONDS_IN_HOUR
        val minutes = (time % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val seconds = time % SECONDS_IN_MINUTE
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
