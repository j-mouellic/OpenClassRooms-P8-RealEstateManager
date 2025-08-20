package com.julien.mouellic.realestatemanager.ui.screen.loancalculator

import com.julien.mouellic.realestatemanager.ui.form.state.FieldState

sealed class LoanCalculatorUIState {

    data class IsLoading(
        val formState: FormState
    ) : LoanCalculatorUIState()

    data class Success(
        val monthlyPayment: Double,
        val formState: FormState
    ) : LoanCalculatorUIState()

    data class Error(
        val errorMessage: String?,
        val formState: FormState
    ) : LoanCalculatorUIState()

    data class FormState(
        val loanAmount: FieldState,
        val interestRate: FieldState,
        val loanTerm: FieldState,
        val downPayment: FieldState,
        val isFormValid: Boolean
    ) : LoanCalculatorUIState()
}
