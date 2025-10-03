package com.julien.mouellic.realestatemanager.utils

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * DateUtils
 *
 * Utility object for formatting and displaying dates.
 * Supports EU (dd/MM/yyyy) and US (yyyy/MM/dd) formats.
 */
object DateUtils {

    /**
     * Current date format mode
     * 0 = European format (dd/MM/yyyy)
     * 1 = US format (yyyy/MM/dd)
     */
    var formatType = 0

    /**
     * Get today's date as a formatted string
     *
     * @return Formatted current date
     */
    fun today(): String {
        return format(Instant.now())
    }

    /**
     * Format an Instant into a string according to the selected format
     *
     * @param instant The Instant to format
     * @return Formatted date string
     */
    fun format(instant: Instant): String {
        // Select pattern based on formatType
        val pattern = if (formatType == 0) "dd/MM/yyyy" else "yyyy/MM/dd"
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

        // Convert Instant to system timezone and format
        return instant.atZone(ZoneId.systemDefault()).format(formatter)
    }
}
