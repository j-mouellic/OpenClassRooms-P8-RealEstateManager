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
    allCommodities: List<Commodity>,
    selectedCommodities: List<Commodity>,
    onCommoditiesSelected: (List<Commodity>) -> Unit
) {
    Column {
        Text("Select Commodities")

        Spacer(modifier = Modifier.height(8.dp))

        val selectedCommoditiesIds = selectedCommodities.map { it.id }.toSet()

        allCommodities.forEach { commodity ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedCommoditiesIds.contains(commodity.id),
                    onCheckedChange = {
                        val newSelectedCommodities = if (it) {
                            selectedCommodities + commodity
                        } else {
                            selectedCommodities - commodity
                        }
                        onCommoditiesSelected(newSelectedCommodities)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(commodity.name)
            }
        }
    }
}
