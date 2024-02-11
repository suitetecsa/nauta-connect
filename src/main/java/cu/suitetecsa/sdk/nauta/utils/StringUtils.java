package cu.suitetecsa.sdk.nauta.utils;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    public static long toSeconds(@NotNull String time) throws InvalidSessionException {
        if (time.equals("errorop")) {
            throw new InvalidSessionException("La sesión ya no es válida");
        }

        String[] parts = time.split(":");
        long totalSeconds = 0;

        for (String part : parts) {
            int SECONDS_PER_MINUTE = 60;
            totalSeconds = totalSeconds * SECONDS_PER_MINUTE + Long.parseLong(part);
        }

        return totalSeconds;
    }

    public static long toDateMillis(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate(date));
        return calendar.getTimeInMillis();
    }

    public static Date toDate(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).parse(date);
    }

    private static long convertToBytes(@NotNull String sizeValue, @NotNull String sizeUnit) {
        double KILOBYTE = 1024;
        double MEGABYTE = KILOBYTE * KILOBYTE;
        double GIGABYTE = MEGABYTE * KILOBYTE;
        double toNumber = Double.parseDouble(sizeValue.replace(",", "."));

        double multi = toNumber * switch (sizeUnit) {
            case "KB" -> KILOBYTE;
            case "MB" -> MEGABYTE;
            case "GB" -> GIGABYTE;
            default -> throw new IllegalArgumentException("La unidad de tamaño no es válida");
        };

        return (long) multi;
    }

    public static long toBytes(@NotNull String bytes) {
        String[] splitText = bytes.split(" ");
        String sizeUnit = splitText[splitText.length - 1];
        String sizeValue = bytes.replace(" " + sizeUnit, "").replace(" ", "");
        return convertToBytes(sizeValue, sizeUnit.toUpperCase(Locale.ROOT));
    }

    public static double fromPriceString(@NotNull String price) {
        return Double.parseDouble(price.replace("$", "").replace(" CUP", "").replace(",", "."));
    }
}
