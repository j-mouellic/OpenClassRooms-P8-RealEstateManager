package com.julien.mouellic.realestatemanager.utils

object CurrencyUtils {
    var currency = 0 //0 = €, 1 = $

    fun display(amountDollar : Double?): String{
        if (amountDollar == null)
            return ""
        if (currency == 0){
            return Math.round(amountDollar * 1.18).toString() + "€"
        }
        return "$amountDollar$"
    }
}