package com.julien.mouellic.realestatemanager.ui.screen.allproperties.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.julien.mouellic.realestatemanager.ui.component.PropertyDivider
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesUiState
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesViewModel
import kotlin.Long


@Composable
fun SearchPropertiesScreen(
    navController: NavHostController,
    viewModel: AllPropertiesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val types by viewModel.allTypes.collectAsState()
    val commodities by viewModel.allCommodities.collectAsState()
    val searchProps = when (uiState) {
        is AllPropertiesUiState.Success -> (uiState as AllPropertiesUiState.Success).searchProperties
        is AllPropertiesUiState.IsLoading -> (uiState as AllPropertiesUiState.IsLoading).searchProperties
        is AllPropertiesUiState.Error -> (uiState as AllPropertiesUiState.Error).searchProperties
        else -> AllPropertiesUiState.SearchProperties(null, null, null, null, null, null, null, null, null)
    }

    var selectedType by remember { mutableStateOf(searchProps.type) }
    var minPrice by remember { mutableStateOf(searchProps.minPrice?.toString() ?: "") }
    var maxPrice by remember { mutableStateOf(searchProps.maxPrice?.toString() ?: "") }
    var minSurface by remember { mutableStateOf(searchProps.minSurface ?: 20f) }
    var maxSurface by remember { mutableStateOf(searchProps.maxSurface ?: 100f) }
    var minRooms by remember { mutableStateOf(searchProps.minNbRooms ?: 1) }
    var maxRooms by remember { mutableStateOf(searchProps.maxNbRooms ?: 5) }
    var isAvailable by remember { mutableStateOf(searchProps.isAvailable ?: false) }
    var selectedCommodities by remember {
        mutableStateOf(
            searchProps.commodities ?: emptyList<Long>()
        )
    }

    var typeDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Search Properties", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Min Max Price
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = minPrice,
                onValueChange = { minPrice = it },
                label = { Text("Min Price") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxPrice,
                onValueChange = { maxPrice = it },
                label = { Text("Max Price") },
                modifier = Modifier.weight(1f)
            )
        }

        PropertyDivider()

        // --- Min/Max Surface
        RangeSliderInput(
            label = "Surface",
            value = minSurface.toFloat()..maxSurface.toFloat(),
            minValue = 0f,
            maxValue = 400f,
            steps = 50,
            onValueChange = { range ->
                minSurface = range.start
                maxSurface = range.endInclusive
            }
        )

        PropertyDivider()

        // --- Min/Max Rooms
        RangeSliderInput(
            label = "Rooms",
            value = minRooms.toFloat()..maxRooms.toFloat(),
            minValue = 0f,
            maxValue = 10f,
            steps = 10,
            onValueChange = { range ->
                minRooms = range.start.toInt()
                maxRooms = range.endInclusive.toInt()
            }
        )

        PropertyDivider()

        // --- Is available toggle ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Is Available")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
        }

        PropertyDivider()

        // --- Type
        Text("Type")
        Box {
            Button(onClick = { typeDropdownExpanded = true }) {
                Text(selectedType?.let { id -> types.find { it.id == id }?.name } ?: "Select Type")
            }
            DropdownMenu(
                expanded = typeDropdownExpanded,
                onDismissRequest = { typeDropdownExpanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            selectedType = type.id
                            typeDropdownExpanded = false
                        }
                    )
                }
            }
        }

        PropertyDivider()

        // --- Commodities multi-selection ---
        Text("Commodities")
        Column {
            commodities.forEach { commodity ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedCommodities.contains(commodity.id),
                        onCheckedChange = { checked ->
                            selectedCommodities = if (checked) {
                                selectedCommodities + listOfNotNull(commodity.id)
                            } else {
                                selectedCommodities.filter { it != commodity.id }
                            }
                        }
                    )
                    Text(commodity.name)
                }
            }
        }

        // --- Boutons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    viewModel.updateSearchProperties(
                        AllPropertiesUiState.SearchProperties(
                            type = selectedType,
                            minPrice = minPrice.toDoubleOrNull(),
                            maxPrice = maxPrice.toDoubleOrNull(),
                            minSurface = minSurface.toDouble(),
                            maxSurface = maxSurface.toDouble(),
                            minNbRooms = minRooms,
                            maxNbRooms = maxRooms,
                            isAvailable = isAvailable,
                            commodities = selectedCommodities.ifEmpty { null }
                        )
                    )
                    viewModel.searchProperties()
                    navController.navigate("all_properties")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Rechercher")
            }

            Button(
                onClick = {
                    selectedType = null
                    selectedCommodities = emptyList()
                    minPrice = ""
                    maxPrice = ""
                    minSurface = 0.0
                    maxSurface = 200.0
                    minRooms = 1
                    maxRooms = 10
                    isAvailable = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun RangeSliderInput(
    label: String,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    minValue: Float = 0f,
    maxValue: Float = 10f,
    steps: Int = 10
) {
    var sliderPosition by remember { mutableStateOf(value) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("$label : ${sliderPosition.start.toInt()} - ${sliderPosition.endInclusive.toInt()}")
        RangeSlider(
            value = sliderPosition,
            valueRange = minValue..maxValue,
            steps = steps,
            onValueChange = { newRange -> sliderPosition = newRange; onValueChange(newRange) },
        )
    }
}
