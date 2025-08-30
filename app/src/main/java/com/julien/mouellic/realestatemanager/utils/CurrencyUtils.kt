package com.julien.mouellic.realestatemanager.utils

object CurrencyUtils {
    var currency = 0 // 0 = €, 1 = $

    fun display(amountDollar: Double?): String {
        if (amountDollar == null) return ""

        val amount = if (currency == 0) {
            Math.round(amountDollar * 1.18).toInt()
        } else {
            Math.round(amountDollar).toInt()
        }

        val formatted = amount.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        // Retour avec symbole
        return if (currency == 0) "$formatted€" else "$formatted$"
    }
}