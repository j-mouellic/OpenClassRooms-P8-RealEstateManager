package com.julien.mouellic.realestatemanager.ui.screen.loancalculator

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.julien.mouellic.realestatemanager.utils.CurrencyUtils

const val LCS_TAG = "LoanCalculatorScreen"


/**
 * Composable function for displaying the Loan Calculator screen.
 *
 * Observes the [LoanCalculatorViewModel] state and displays:
 *  - Input fields for loan amount, interest rate, loan term, and down payment
 *  - Validation errors for each field
 *  - A button to calculate the monthly payment
 *  - Loading state while calculation is in progress
 *  - Result of the monthly payment
 *  - Error messages if calculation fails or input is invalid
 *
 * @param viewModel Instance of [LoanCalculatorViewModel] provided via Hilt.
 */
@Composable
fun LoanCalculatorScreen(viewModel: LoanCalculatorViewModel = hiltViewModel()) {
    // Observe the UI state from the ViewModel
    val uiState = viewModel.uiState.collectAsState().value

    // Extract the current form state regardless of UI state type
    val formState = when (uiState) {
        is LoanCalculatorUIState.FormState -> uiState
        is LoanCalculatorUIState.Success -> uiState.formState
        is LoanCalculatorUIState.Error -> uiState.formState
        is LoanCalculatorUIState.IsLoading -> uiState.formState
    }

    // Debug logging for UI state changes
    LaunchedEffect(uiState) {
        Log.d(LCS_TAG, "UIState changed: $uiState")
    }

    Column(modifier = Modifier.padding(16.dp)) {

        val loanAmount = formState.loanAmount
        val interestRate = formState.interestRate
        val loanTerm = formState.loanTerm
        val downPayment = formState.downPayment

        // Screen title
        Text("Loan Calculator")
        Spacer(modifier = Modifier.height(16.dp))

        // Loan Amount input field with validation
        OutlinedTextField(
            value = loanAmount.value ?: "",
            onValueChange = { viewModel.updateFieldValue("loanAmount", it) },
            label = { Text("Loan Amount") },
            modifier = Modifier.fillMaxWidth(),
            isError = !loanAmount.isValid,
            supportingText = { loanAmount.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Down Payment input field with validation
        OutlinedTextField(
            value = downPayment.value ?: "",
            onValueChange = { viewModel.updateFieldValue("downPayment", it) },
            label = { Text("Down Payment") },
            modifier = Modifier.fillMaxWidth(),
            isError = !downPayment.isValid,
            supportingText = { downPayment.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Interest Rate input field with validation
        OutlinedTextField(
            value = interestRate.value ?: "",
            onValueChange = { viewModel.updateFieldValue("interestRate", it) },
            label = { Text("Interest Rate") },
            modifier = Modifier.fillMaxWidth(),
            isError = !interestRate.isValid,
            supportingText = { interestRate.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Loan Term input field with validation
        OutlinedTextField(
            value = loanTerm.value ?: "",
            onValueChange = { viewModel.updateFieldValue("loanTerm", it) },
            label = { Text("Loan Term (Years)") },
            modifier = Modifier.fillMaxWidth(),
            isError = !loanTerm.isValid,
            supportingText = { loanTerm.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate button enabled only if form is valid
        Button(
            onClick = { viewModel.onCalculateClicked() },
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.isFormValid
        ) {
            Text("Calculate Loan")
        }

        // Show different content based on the current UI state
        when (uiState) {
            is LoanCalculatorUIState.IsLoading -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading...") // Display loading message
            }

            is LoanCalculatorUIState.Success -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Calculated Monthly Payment: ${CurrencyUtils.display(uiState.monthlyPayment)}") // Display result
            }

            is LoanCalculatorUIState.Error -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Error: ${uiState.errorMessage ?: "Unknown error"}") // Display error message
            }

            is LoanCalculatorUIState.FormState -> {
                // No extra UI needed when just showing the form
            }
        }
    }
}