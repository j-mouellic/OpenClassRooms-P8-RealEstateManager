package com.julien.mouellic.realestatemanager.ui.screen.loancalculator

import com.julien.mouellic.realestatemanager.ui.form.state.FieldState

/**
 * Represents the different UI states for the Loan Calculator screen.
 *
 * The UI observes an instance of [LoanCalculatorUIState] and updates accordingly:
 *  - Displays the form,
 *  - Shows a loading indicator during calculation,
 *  - Shows the result of the monthly payment,
 *  - Or shows validation/error messages.
 */
sealed class LoanCalculatorUIState {

    /**
     * State displayed while the monthly payment calculation is in progress.
     *
     * @property formState Current form values and validation status.
     */
    data class IsLoading(
        val formState: FormState
    ) : LoanCalculatorUIState()

    /**
     * State displayed when the calculation succeeds.
     *
     * @property monthlyPayment The calculated monthly payment.
     * @property formState Current form values and validation status.
     */
    data class Success(
        val monthlyPayment: Double,
        val formState: FormState
    ) : LoanCalculatorUIState()

    /**
     * State displayed when there is an error or invalid input.
     *
     * @property errorMessage Optional error message describing the problem.
     * @property formState Current form values and validation status.
     */
    data class Error(
        val errorMessage: String?,
        val formState: FormState
    ) : LoanCalculatorUIState()

    /**
     * State representing the current form values and their validation.
     *
     * @property loanAmount Loan amount input as a [FieldState].
     * @property interestRate Interest rate input as a [FieldState].
     * @property loanTerm Loan term (years) input as a [FieldState].
     * @property downPayment Down payment input as a [FieldState].
     * @property isFormValid True if all fields are valid.
     */
    data class FormState(
        val loanAmount: FieldState,
        val interestRate: FieldState,
        val loanTerm: FieldState,
        val downPayment: FieldState,
        val isFormValid: Boolean
    ) : LoanCalculatorUIState()
}
