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

const val LCS_TAG = "LoanCalculatorScreen"

@Composable
fun LoanCalculatorScreen(viewModel: LoanCalculatorViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    val formState = when (uiState) {
        is LoanCalculatorUIState.FormState -> uiState
        is LoanCalculatorUIState.Success -> uiState.formState
        is LoanCalculatorUIState.Error -> uiState.formState
        is LoanCalculatorUIState.IsLoading -> uiState.formState
    }

    LaunchedEffect(uiState) {
        Log.d(LCS_TAG, "UIState changed: $uiState")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        val loanAmount = formState.loanAmount
        val interestRate = formState.interestRate
        val loanTerm = formState.loanTerm
        val downPayment = formState.downPayment

        Text("Loan Calculator")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = loanAmount.value ?: "",
            onValueChange = { viewModel.updateFieldValue("loanAmount", it) },
            label = { Text("Loan Amount") },
            modifier = Modifier.fillMaxWidth(),
            isError = !loanAmount.isValid,
            supportingText = { loanAmount.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = downPayment.value ?: "",
            onValueChange = { viewModel.updateFieldValue("downPayment", it) },
            label = { Text("Down Payment") },
            modifier = Modifier.fillMaxWidth(),
            isError = !downPayment.isValid,
            supportingText = { downPayment.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = interestRate.value ?: "",
            onValueChange = { viewModel.updateFieldValue("interestRate", it) },
            label = { Text("Interest Rate") },
            modifier = Modifier.fillMaxWidth(),
            isError = !interestRate.isValid,
            supportingText = { interestRate.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = loanTerm.value ?: "",
            onValueChange = { viewModel.updateFieldValue("loanTerm", it) },
            label = { Text("Loan Term (Years)") },
            modifier = Modifier.fillMaxWidth(),
            isError = !loanTerm.isValid,
            supportingText = { loanTerm.errorMessage?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onCalculateClicked() },
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.isFormValid
        ) {
            Text("Calculate Loan")
        }

        when (uiState) {
            is LoanCalculatorUIState.IsLoading -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading...")
            }

            is LoanCalculatorUIState.Success -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Calculated Monthly Payment: ${uiState.monthlyPayment}")
            }

            is LoanCalculatorUIState.Error -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Error: ${uiState.errorMessage ?: "Unknown error"}")
            }

            is LoanCalculatorUIState.FormState -> { }
        }
    }
}