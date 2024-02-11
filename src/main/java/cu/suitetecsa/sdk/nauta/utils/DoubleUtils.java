package cu.suitetecsa.sdk.nauta.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class DoubleUtils {
    public static @NotNull String toPriceString(double price) {
        return String.format(Locale.getDefault(), "$%.2f CUP", price);
    }
}
