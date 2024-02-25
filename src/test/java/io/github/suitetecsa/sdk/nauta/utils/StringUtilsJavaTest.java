package io.github.suitetecsa.sdk.nauta.utils;

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import org.junit.Test;
import org.junit.Assert;
import java.text.ParseException;

public class StringUtilsJavaTest {

    @Test
    public void toSecondsWithValidInputReturnsSeconds() throws InvalidSessionException {
        String input = "00:01:30";
        long expected = 90L;
        long actual = StringUtils.toSeconds(input);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = InvalidSessionException.class)
    public void toSecondsWithInvalidSessionThrowsException() throws InvalidSessionException {
        String input = "errorop";
        StringUtils.toSeconds(input);
    }

    @Test
    public void toSecondsWithEmptyStringReturns0() throws InvalidSessionException {
        String input = "";
        long expected = 0L;
        long actual = StringUtils.toSeconds(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toSecondsWithOnlyMinutesReturnsSeconds() throws InvalidSessionException {
        String input = "00:05:00";
        long expected = 300L;
        long actual = StringUtils.toSeconds(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toSecondsWithOnlySecondsReturnsSeconds() throws InvalidSessionException {
        String input = "00:00:30";
        long expected = 30L;
        long actual = StringUtils.toSeconds(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toDateMillisWithValidDateReturnsMilliseconds() throws ParseException {
        String input = "01/01/2023";
        long expected = 1672549200000L; // Este valor puede variar según la zona horaria.
        long actual = StringUtils.toDateMillis(input);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void toDateMillisWithInvalidDateThrowsParseException() throws ParseException {
        String input = "01322023";
        StringUtils.toDateMillis(input);
    }

    @Test
    public void toBytesWithValidInputReturnsBytes() {
        String input = "5 MB";
        long expected = 5242880L;
        long actual = StringUtils.toBytes(input);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toBytesWithInvalidUnitThrowsException() {
        String input = "5 GBH";
        StringUtils.toBytes(input);
    }

    @Test
    public void toBytesWithDecimalValueParsesCorrectly() {
        String input = "5.5 MB";
        long expected = 5767168L;
        long actual = StringUtils.toBytes(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toBytesWithSpacesParsesCorrectly() {
        String input = "4 KB ";
        long expected = 4096L;
        long actual = StringUtils.toBytes(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromPriceStringWithValidInputReturnsDouble() {
        String input = "$5.00 CUP";
        double expected = 5.0;

        double actual = StringUtils.fromPriceString(input);

        Assert.assertEquals(expected, actual, 0.0);
    }

    @Test(expected = ParseException.class)
    public void fromPriceStringWithInvalidInputThrowsException() {
        StringUtils.fromPriceString("invalid");
    }
}
