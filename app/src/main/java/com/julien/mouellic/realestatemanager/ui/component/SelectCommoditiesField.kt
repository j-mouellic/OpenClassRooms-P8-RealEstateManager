package com.julien.mouellic.realestatemanager.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julien.mouellic.realestatemanager.domain.model.Commodity

@Composable
fun SelectCommoditiesField(
    allCommodities: List<Commodity>,               // Full list of available commodities (from DB or repository)
    selectedCommodities: List<Commodity>,          // Currently selected commodities
    onCommoditiesSelected: (List<Commodity>) -> Unit // Callback triggered when selection changes
) {
    // --- Container column for the entire component ---
    Column {
        // --- Section title ---
        Text("Select Commodities")

        Spacer(modifier = Modifier.height(8.dp))

        // --- Create a fast lookup set for selected commodity IDs ---
        val selectedCommoditiesIds = selectedCommodities.map { it.id }.toSet()

        // --- Display each commodity as a checkbox row ---
        allCommodities.forEach { commodity ->
            Row(
                verticalAlignment = Alignment.CenterVertically // Align checkbox + text vertically
            ) {
                // --- Checkbox for selecting/deselecting a commodity ---
                Checkbox(
                    checked = selectedCommoditiesIds.contains(commodity.id), // Is this commodity already selected?
                    onCheckedChange = { isChecked ->
                        // Compute new list of selected commodities based on checkbox state
                        val newSelectedCommodities = if (isChecked) {
                            selectedCommodities + commodity   // Add if checked
                        } else {
                            selectedCommodities - commodity   // Remove if unchecked
                        }
                        // Trigger callback with updated list
                        onCommoditiesSelected(newSelectedCommodities)
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // --- Display the name of the commodity next to its checkbox ---
                Text(commodity.name)
            }
        }
    }
}

