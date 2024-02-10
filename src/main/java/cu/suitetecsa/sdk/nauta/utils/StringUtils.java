package cu.suitetecsa.sdk.nauta.utils;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;

public class StringUtils {

    public static long toSeconds(String time) throws InvalidSessionException {
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
}
