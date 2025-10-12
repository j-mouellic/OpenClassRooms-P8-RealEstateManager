package com.julien.mouellic.realestatemanager.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.julien.mouellic.realestatemanager.utils.DateUtils
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstantDateSelectionField(
    label: String,                     // Text label displayed above the input field
    selectedInstant: Instant?,         // Currently selected date as an Instant (nullable)
    onDateSelected: (Instant?) -> Unit // Callback triggered when a new date is chosen
) {
    // --- Set up date formatting ---
    // Define the timezone and date format used to display the date in the text field.
    val zoneId = ZoneId.systemDefault()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .withZone(zoneId)

    // --- Convert Instant to formatted String for display ---
    val formattedDate = selectedInstant?.let {
        DateUtils.format(it) // Use app’s DateUtils to respect user’s chosen format (EU/US)
    } ?: ""

    // --- Initialize the date picker with the current or selected date ---
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedInstant?.toEpochMilli()
            ?: Calendar.getInstance().timeInMillis
    )

    // --- Controls whether the date picker popup is currently visible ---
    var showDatePicker by remember { mutableStateOf(false) }

    // --- Main UI container ---
    Box(modifier = Modifier.fillMaxWidth()) {

        // --- Read-only text field showing the currently selected date ---
        OutlinedTextField(
            value = formattedDate,
            onValueChange = { }, // Field is read-only, no manual typing allowed
            label = { Text(label) }, // Display the given label above the field
            readOnly = true,
            trailingIcon = {
                // --- Calendar icon button ---
                // When pressed, toggles the date picker popup visibility
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        // --- Popup for date selection ---
        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false }, // Close popup when clicking outside
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp) // Position below the text field
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {

                    // --- Actual Date Picker Component ---
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false // Simplify: only calendar mode
                    )

                    // --- Row of action buttons below the date picker ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Cancel button → closes popup without changes
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        // Reset button → clears selected date (sets it to null)
                        TextButton(
                            onClick = {
                                showDatePicker = false
                                onDateSelected(null)
                            }
                        ) {
                            Text("Reset")
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        // OK button → confirms date and sends Instant to parent composable
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val instant = Instant.ofEpochMilli(millis)
                                    onDateSelected(instant)
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

