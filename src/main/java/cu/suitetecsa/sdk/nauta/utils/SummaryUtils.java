package cu.suitetecsa.sdk.nauta.utils;

import cu.suitetecsa.sdk.nauta.model.Summary;
import org.jetbrains.annotations.NotNull;

public class SummaryUtils {
    private static final int PAGE_MAX_LENGTH = 14;

    public static <T extends Summary> int getPagesCount(@NotNull T summary) {
        return (int) Math.ceil((double) summary.count() / PAGE_MAX_LENGTH);
    }
}
