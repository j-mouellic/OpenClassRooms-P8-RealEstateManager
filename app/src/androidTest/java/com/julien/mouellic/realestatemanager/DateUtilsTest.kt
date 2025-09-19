package com.julien.mouellic.realestatemanager

import com.julien.mouellic.realestatemanager.utils.DateUtils
import org.threeten.bp.Instant
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun formatEuDate() {
        DateUtils.formatType = 0
        val instant = Instant.parse("2025-09-12T10:15:30Z") // date fixe

        val formatted = DateUtils.format(instant)

        assertEquals("12/09/2025", formatted)
    }

    @Test
    fun formatUsDate() {
        DateUtils.formatType = 1
        val instant = Instant.parse("2025-09-12T10:15:30Z")

        val formatted = DateUtils.format(instant)

        assertEquals("2025/09/12", formatted)
    }
}