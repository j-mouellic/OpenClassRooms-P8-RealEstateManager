package com.julien.mouellic.realestatemanager.domain.usecase.loan

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

/**
 * Use Case: LoanCalculatorUseCase
 *
 * In Clean Architecture, a Use Case represents a specific piece of business logic.
 * This class encapsulates the logic for calculating monthly loan payments based on
 * amount, interest rate, down payment, and loan term.
 *
 * Responsibilities:
 * - Encapsulate the formula for mortgage/loan monthly payments.
 * - Keep calculation logic separate from UI or repository layers.
 * - Ensure values are rounded to 2 decimal places for financial correctness.
 *
 * Benefits:
 * - Testable independently of UI or data sources.
 * - Maintains Single Responsibility Principle (SRP) by focusing on loan calculation only.
 */
class LoanCalculatorUseCase {
    companion object {
        const val MONTHS = 12       // Number of months in a year
        const val ONE = 1           // Used in calculation formula
    }

    /**
     * Calculate the monthly payment for a loan.
     *
     * @param amount Total price of the property or loan principal
     * @param interestRate Annual interest rate (percentage)
     * @param downPayment Initial down payment
     * @param loanTerm Loan term in years
     * @return Double: monthly payment rounded to 2 decimal places
     */
    fun calculateMonthlyPayment(
        amount: Double,
        interestRate: Double,
        downPayment: Double,
        loanTerm: Double
    ): Double {
        val remainingAmount = amount - downPayment  // Principal remaining after down payment
        val monthlyRate = interestRate / 100 / MONTHS  // Convert annual % rate to monthly decimal
        val numberOfPayments = loanTerm * MONTHS      // Total number of monthly payments

        // Standard formula for fixed-rate loan monthly payment
        val perMonth =
            (remainingAmount * monthlyRate) / (ONE - (ONE + monthlyRate).pow(-numberOfPayments))

        // Round to 2 decimals for currency
        val roundedValue = BigDecimal(perMonth).setScale(2, RoundingMode.HALF_UP).toDouble()

        return roundedValue
    }
}
