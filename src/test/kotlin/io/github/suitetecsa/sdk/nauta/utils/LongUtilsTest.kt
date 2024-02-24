package io.github.suitetecsa.sdk.nauta.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import io.github.suitetecsa.sdk.nauta.utils.LongUtils

class LongUtilsTest {

  @Test
  fun `toDateString converts long to date string`() {
    val calendar = Calendar.getInstance()
    calendar.set(2020, 1, 15)
    assertEquals("15/02/20", LongUtils.toDateString(calendar.timeInMillis))
  }

  @Test 
  fun `toDateString handles single digit days and months`() {
    val calendar = Calendar.getInstance()
    calendar.set(2020, 1, 5)
    assertEquals("05/02/20", LongUtils.toDateString(calendar.timeInMillis))
  }
}
