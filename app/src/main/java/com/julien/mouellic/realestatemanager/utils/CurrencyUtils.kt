package com.julien.mouellic.realestatemanager.utils

/**
 * CurrencyUtils
 *
 * Utility object for formatting and displaying monetary amounts
 * in either Euro (€) or Dollar ($). Also handles simple conversion
 * from Dollar to Euro using a fixed rate.
 */
object CurrencyUtils {

    /**
     * Current currency mode
     * 0 = Euro (€)
     * 1 = Dollar ($)
     */
    var currency = 0

    /**
     * Display a monetary amount as a formatted string with appropriate currency symbol.
     *
     * @param amountDollar Amount in dollars
     * @return Formatted string with thousand separators and currency symbol
     */
    fun display(amountDollar: Double?): String {
        if (amountDollar == null) return "" // Return empty string for null values

        // Convert to desired currency
        val amount = if (currency == 0) {
            // Convert dollars to euros (fixed rate 1.18)
            Math.round(amountDollar * 1.18).toInt()
        } else {
            // Keep in dollars
            Math.round(amountDollar).toInt()
        }

        // Format with thousand separators (e.g., 1234567 -> 1 234 567)
        val formatted = amount.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        // Append currency symbol
        return if (currency == 0) "$formatted€" else "$formatted$"
    }
}
