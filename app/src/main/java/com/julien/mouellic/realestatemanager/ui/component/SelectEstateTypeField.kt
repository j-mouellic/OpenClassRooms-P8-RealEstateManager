package com.julien.mouellic.realestatemanager.ui.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

@Composable
fun SelectEstateTypeField(
    allEstateTypes: List<RealEstateType>,              // All available estate types (from DB or repository)
    selectedEstateType: RealEstateType?,               // Currently selected estate type (nullable)
    onEstateTypeSelected: (RealEstateType) -> Unit     // Callback triggered when user selects one
) {
    // --- UI state ---
    var isDropdownOpen by remember { mutableStateOf(false) } // Controls dropdown visibility
    var textFieldValue by remember {
        mutableStateOf(selectedEstateType?.name ?: "Select...") // Initial displayed value
    }

    // --- Main container ---
    Column {
        // --- Label ---
        Text("Select Estate Type *")

        Spacer(modifier = Modifier.height(8.dp))

        // --- Read-only text field that opens the dropdown when clicked ---
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText -> textFieldValue = newText }, // Not used since field is read-only
            label = { Text("Estate Type") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                // --- Dropdown toggle icon ---
                IconButton(onClick = { isDropdownOpen = !isDropdownOpen }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )

        // --- Dropdown list containing all estate types ---
        DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            allEstateTypes.forEach { estateType ->
                DropdownMenuItem(
                    onClick = {
                        // When user clicks one:
                        onEstateTypeSelected(estateType)  // Notify parent composable
                        textFieldValue = estateType.name  // Update text field
                        isDropdownOpen = false            // Close menu
                    },
                    text = { Text(estateType.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Display current selection below ---
        if (selectedEstateType != null) {
            Text("Selected Estate Type: ${selectedEstateType.name}")
        }
    }
}

