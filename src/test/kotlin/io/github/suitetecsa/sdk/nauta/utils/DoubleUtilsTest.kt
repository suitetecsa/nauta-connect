package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.utils.DoubleUtils.toPriceString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DoubleUtilsTest {

  @Test
  fun `toPriceString converts double to formatted string`() {
    val price = 10.99
    val expected = "$10.99 CUP"
  
    assertEquals(expected, toPriceString(price)) 
  }

  @Test
  fun `toPriceString handles 0 price`() {
    val price = 0.0
    val expected = "$0.00 CUP"
  
    assertEquals(expected, toPriceString(price))
  }

  @Test
  fun `toTimeString converts seconds to time string`() {
    val result = LongUtils.toTimeString(3600)
    assertEquals("01:00:00", result)
  }

  @Test
  fun `toTimeString handles single digit hours`() {
    val result = LongUtils.toTimeString(3600 * 5)
    assertEquals("05:00:00", result)
  }

  @Test
  fun `toTimeString handles single digit minutes and seconds`() {
    val result = LongUtils.toTimeString(3900)
    assertEquals("01:05:00", result)
  }

  @Test
  fun `toTimeString returns 00 for 0 input`() {
    val result = LongUtils.toTimeString(0)
    assertEquals("00:00:00", result)
  }
}
