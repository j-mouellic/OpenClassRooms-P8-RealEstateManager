package com.julien.mouellic.realestatemanager

import com.julien.mouellic.realestatemanager.utils.CurrencyUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun testNullAmount() {
        CurrencyUtils.currency = 0
        val result = CurrencyUtils.display(null)
        assertEquals("", result)
    }

    @Test
    fun testEuroDisplay() {
        CurrencyUtils.currency = 0
        // 100 dollars -> (100 * 1.18 = 118€)
        val result = CurrencyUtils.display(100.0)
        assertEquals("118€", result)
    }

    @Test
    fun testDollarDisplay() {
        CurrencyUtils.currency = 1
        val result = CurrencyUtils.display(1000.0)
        // 1000 => 1000$
        assertEquals("1 000$", result)
    }

    @Test
    fun testLargeNumberDisplay() {
        CurrencyUtils.currency = 1
        val result = CurrencyUtils.display(1234567.89)
        assertEquals("1 234 568$", result)
    }
}
