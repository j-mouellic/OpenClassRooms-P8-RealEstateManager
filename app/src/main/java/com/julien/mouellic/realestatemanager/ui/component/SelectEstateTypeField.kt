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
    allEstateTypes: List<RealEstateType>,
    selectedEstateType: RealEstateType?,
    onEstateTypeSelected: (RealEstateType) -> Unit
) {
    var isDropdownOpen by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(
        selectedEstateType?.name ?: "Select..."
    ) }

    Column {
        Text("Select Estate Type *")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText -> textFieldValue = newText },
            label = { Text("Estate Type") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isDropdownOpen = !isDropdownOpen }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            allEstateTypes.forEach { estateType ->
                DropdownMenuItem(
                    onClick = {
                        onEstateTypeSelected(estateType)
                        textFieldValue = estateType.name
                        isDropdownOpen = false
                    },
                    text = { Text(estateType.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedEstateType != null) {
            Text("Selected Estate Type: ${selectedEstateType.name}")
        }
    }
}
