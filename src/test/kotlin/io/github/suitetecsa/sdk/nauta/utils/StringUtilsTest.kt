package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import java.text.ParseException

class StringUtilsTest {

    @Test
    fun `toSeconds with valid input returns seconds`() {
        val input = "00:01:30"
        val expected = 90L
        val actual = StringUtils.toSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toSeconds with invalid session throws exception`() {
        val input = "errorop"

        assertThrows(InvalidSessionException::class.java) {
            StringUtils.toSeconds(input)
        }
    }

    @Test
    fun `toSeconds with empty string returns 0`() {
        val input = ""
        val expected = 0L
        val actual = StringUtils.toSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toSeconds with only minutes returns seconds`() {
        val input = "00:05:00"
        val expected = 300L
        val actual = StringUtils.toSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toSeconds with only seconds returns seconds`() {
        val input = "00:00:30"
        val expected = 30L
        val actual = StringUtils.toSeconds(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toDateMillis with valid date returns milliseconds`() {
        val input = "01/01/2023"
        val expected = 1672549200000

        val actual = StringUtils.toDateMillis(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toDateMillis with invalid date throws ParseException`() {
        val input = "01322023"

        assertThrows(ParseException::class.java) {
            StringUtils.toDateMillis(input)
        }
    }

    @Test
    fun `toBytes with valid input returns bytes`() {
        val input = "5 MB"
        val expected = 5242880L

        val actual = StringUtils.toBytes(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toBytes with invalid unit throws exception`() {
        val input = "5 GBH"

        assertThrows(IllegalArgumentException::class.java) {
            StringUtils.toBytes(input)
        }
    }

    @Test
    fun `toBytes with decimal value parses correctly`() {
        val input = "5.5 MB"
        val expected = 5767168L

        val actual = StringUtils.toBytes(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `toBytes with spaces parses correctly`() {
        val input = "4 KB "
        val expected = 4096L

        val actual = StringUtils.toBytes(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `fromPriceString with valid input returns double`() {
        val input = "$5.00 CUP"
        val expected = 5.0

        val actual = StringUtils.fromPriceString(input)

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun `fromPriceString with invalid input returns 0,0`() {
        assertThrows(ParseException::class.java) {
            StringUtils.fromPriceString("invalid")
        }
    }
}
