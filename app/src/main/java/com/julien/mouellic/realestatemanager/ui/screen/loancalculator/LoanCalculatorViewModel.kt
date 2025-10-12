package com.julien.mouellic.realestatemanager.ui.screen.loancalculator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.usecase.loan.LoanCalculatorUseCase
import com.julien.mouellic.realestatemanager.ui.form.converter.FormConverter
import com.julien.mouellic.realestatemanager.ui.form.formater.FormFormater
import com.julien.mouellic.realestatemanager.ui.form.state.FieldState
import com.julien.mouellic.realestatemanager.ui.form.validator.FormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Loan Calculator screen.
 *
 * Responsibilities:
 * 1. Hold the current form state for loan calculation inputs (loan amount, interest rate, term, down payment).
 * 2. Validate user input using [FormValidator].
 * 3. Convert string inputs to numeric types using [FormConverter].
 * 4. Trigger calculation of monthly payments using [LoanCalculatorUseCase].
 * 5. Expose the current UI state as [LoanCalculatorUIState].
 *
 * The UI observes [uiState] to reactively display:
 * - the input form,
 * - a loading state,
 * - calculation results,
 * - or validation/error messages.
 */
@HiltViewModel
class LoanCalculatorViewModel @Inject constructor(
    private val loanCalculatorUseCase: LoanCalculatorUseCase,
    private val formValidator: FormValidator,
    private val formConverter: FormConverter,
    private val formFormater: FormFormater
) : ViewModel() {

    companion object {
        const val TAG = "LoanCalculatorViewModel"

        // Validation constants for form fields
        const val LOAN_AMOUNT_IS_REQUIRED = true
        const val LOAN_AMOUNT_MIN = 1.0
        const val LOAN_AMOUNT_MAX = 10000000.0

        const val INTEREST_RATE_IS_REQUIRED = true
        const val INTEREST_RATE_MIN = 0.0
        const val INTEREST_RATE_MAX = 100.0

        const val LOAN_TERM_IS_REQUIRED = true
        const val LOAN_TERM_MIN = 0.0
        const val LOAN_TERM_MAX = 100.0

        const val DOWN_PAYMENT_IS_REQUIRED = true
        const val DOWN_PAYMENT_MIN = 0.0
        const val DOWN_PAYMENT_MAX = 10000000.0
    }

    // Holds the current UI state
    private val _uiState = MutableStateFlow<LoanCalculatorUIState>(
        LoanCalculatorUIState.FormState(
            loanAmount = FieldState("100000", true),
            interestRate = FieldState("3.5", true),
            loanTerm = FieldState("20", true),
            downPayment = FieldState("0", true),
            isFormValid = true
        )
    )
    val uiState: StateFlow<LoanCalculatorUIState> = _uiState

    /** Returns the current form state regardless of UI state wrapper */
    private fun getFormState(): LoanCalculatorUIState.FormState {
        return when (val currentState = _uiState.value) {
            is LoanCalculatorUIState.IsLoading -> currentState.formState
            is LoanCalculatorUIState.Error -> currentState.formState
            is LoanCalculatorUIState.FormState -> currentState
            is LoanCalculatorUIState.Success -> currentState.formState
        }
    }

    /**
     * Updates a field value in the form and validates it.
     *
     * @param fieldName name of the field to update ("loanAmount", "interestRate", "loanTerm", "downPayment")
     * @param newValue new string value entered by the user
     */
    fun updateFieldValue(fieldName: String, newValue: String) {
        viewModelScope.launch {
            val currentState = getFormState()
            val updatedState = when (fieldName) {
                "loanAmount" -> currentState.copy(
                    loanAmount = formValidator.validateDouble(newValue, LOAN_AMOUNT_MIN, LOAN_AMOUNT_MAX, LOAN_AMOUNT_IS_REQUIRED)
                )
                "interestRate" -> currentState.copy(
                    interestRate = formValidator.validateDouble(newValue, INTEREST_RATE_MIN, INTEREST_RATE_MAX, INTEREST_RATE_IS_REQUIRED)
                )
                "loanTerm" -> currentState.copy(
                    loanTerm = formValidator.validateDouble(newValue, LOAN_TERM_MIN, LOAN_TERM_MAX, LOAN_TERM_IS_REQUIRED)
                )
                "downPayment" -> currentState.copy(
                    downPayment = formValidator.validateDouble(newValue, DOWN_PAYMENT_MIN, DOWN_PAYMENT_MAX, DOWN_PAYMENT_IS_REQUIRED)
                )
                else -> currentState
            }
            val isFormValid = isFormValid(updatedState)
            _uiState.value = updatedState.copy(isFormValid = isFormValid)
        }
    }

    /** Validates all fields in the form */
    private fun validateAll() {
        var currentState = getFormState()

        currentState = currentState.copy(
            loanAmount = formValidator.validateDouble(currentState.loanAmount.value, LOAN_AMOUNT_MIN, LOAN_AMOUNT_MAX, LOAN_AMOUNT_IS_REQUIRED),
            interestRate = formValidator.validateDouble(currentState.interestRate.value, INTEREST_RATE_MIN, INTEREST_RATE_MAX, INTEREST_RATE_IS_REQUIRED),
            loanTerm = formValidator.validateDouble(currentState.loanTerm.value, LOAN_TERM_MIN, LOAN_TERM_MAX, LOAN_TERM_IS_REQUIRED),
            downPayment = formValidator.validateDouble(currentState.downPayment.value, DOWN_PAYMENT_MIN, DOWN_PAYMENT_MAX, DOWN_PAYMENT_IS_REQUIRED)
        )

        currentState = currentState.copy(isFormValid = isFormValid(currentState))
        _uiState.value = currentState
    }

    /** Returns true if all form fields are valid */
    private fun isFormValid(state: LoanCalculatorUIState.FormState): Boolean {
        Log.d(TAG, "loanAmount: ${state.loanAmount.isValid}")
        Log.d(TAG, "interestRate: ${state.interestRate.isValid}")
        Log.d(TAG, "loanTerm: ${state.loanTerm.isValid}")
        Log.d(TAG, "downPayment: ${state.downPayment.isValid}")
        return state.loanAmount.isValid &&
                state.interestRate.isValid &&
                state.loanTerm.isValid &&
                state.downPayment.isValid
    }

    /**
     * Triggered when the "Calculate" button is clicked.
     *
     * Steps:
     * 1. Validate all fields.
     * 2. If the form is valid, calculate the monthly loan payment.
     * 3. Update the UI state to show result or error.
     */
    fun onCalculateClicked() {
        validateAll()
        val formState = getFormState()
        if (formState.isFormValid) {
            _uiState.value = LoanCalculatorUIState.IsLoading(formState)
            viewModelScope.launch {
                try {
                    val loanAmount = formConverter.toDouble(formState.loanAmount.value) ?: 0.0
                    val interestRate = formConverter.toDouble(formState.interestRate.value) ?: 0.0
                    val loanTerm = formConverter.toDouble(formState.loanTerm.value) ?: 0.0
                    val downPayment = formConverter.toDouble(formState.downPayment.value) ?: 0.0

                    Log.d(TAG, "loanAmount: $loanAmount")
                    Log.d(TAG, "interestRate: $interestRate")
                    Log.d(TAG, "loanTerm: $loanTerm")
                    Log.d(TAG, "downPayment: $downPayment")

                    val monthlyPayment = loanCalculatorUseCase.calculateMonthlyPayment(
                        loanAmount, interestRate, downPayment, loanTerm
                    )

                    _uiState.value = LoanCalculatorUIState.Success(monthlyPayment, formState)
                } catch (e: Exception) {
                    _uiState.value = LoanCalculatorUIState.Error(e.message, formState)
                }
            }
        } else {
            _uiState.value = LoanCalculatorUIState.Error("Invalid input values", formState)
        }
    }
}
