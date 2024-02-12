package io.github.suitetecsa.sdk.nauta.utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LongUtils {
    public static @NotNull String toDateString(Long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(calendar.getTime());
    }

    public static String toTimeString(Long time) {
        int SECONDS_IN_HOUR = 3600;
        int SECONDS_IN_MINUTE = 60;
        long hours = time / SECONDS_IN_HOUR;
        long minutes = (time % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        long seconds = time % SECONDS_IN_MINUTE;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
