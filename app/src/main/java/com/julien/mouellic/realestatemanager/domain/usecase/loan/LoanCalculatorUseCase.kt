package com.julien.mouellic.realestatemanager.domain.usecase.loan

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

class LoanCalculatorUseCase {
    companion object {
        const val MONTHS = 12
        const val ONE = 1
    }

    fun calculateMonthlyPayment(
        amount: Double,
        interestRate: Double,
        downPayment: Double,
        loanTerm: Double
    ): Double {
        val remainingAmount = amount - downPayment
        val monthlyRate = interestRate / 100 / MONTHS
        val numberOfPayments = loanTerm * MONTHS
        val perMonth =
            (remainingAmount * monthlyRate) / (ONE - (ONE + monthlyRate).pow(-numberOfPayments))
        val roundedValue = BigDecimal(perMonth).setScale(2, RoundingMode.HALF_UP).toDouble()
        return roundedValue
    }
}