package com.julien.mouellic.realestatemanager.ui.screen.searchproperty

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.ui.component.LoadingScreen
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.formatWithSpaces

@Composable
fun SearchPropertiesScreen(
    navController: NavHostController,
    viewModel: SearchPropertiesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val searchProps = (uiState as? SearchPropertiesUIState.WaitingForUserInteraction)?.searchProperties
        ?: (uiState as? SearchPropertiesUIState.IsLoading)?.searchProperties
        ?: (uiState as? SearchPropertiesUIState.Success)?.searchProperties
        ?: (uiState as? SearchPropertiesUIState.Error)?.searchProperties

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ---------- Header / Search Form ----------
        item {
            Text("Search Properties", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            searchProps?.let { sp ->
                // Row: Min/Max Price
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sp.minPrice?.toString() ?: "",
                        onValueChange = { value ->
                            viewModel.updateSearchProperties { copy(minPrice = value.toDoubleOrNull()) }
                        },
                        label = { Text("Min Price") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sp.maxPrice?.toString() ?: "",
                        onValueChange = { value ->
                            viewModel.updateSearchProperties { copy(maxPrice = value.toDoubleOrNull()) }
                        },
                        label = { Text("Max Price") },
                        modifier = Modifier.weight(1f)
                    )
                }

                PropertyDivider()

                // Row: Min/Max Surface
                RangeSliderInput(
                    label = "Surface",
                    value = (sp.minSurface?.toFloat() ?: 50f)..(sp.maxSurface?.toFloat() ?: 100f),
                    minValue = 0f,
                    maxValue = 400f,
                    steps = 50,
                    onValueChange = { range ->
                        viewModel.updateSearchProperties {
                            copy(
                                minSurface = range.start.toDouble(),
                                maxSurface = range.endInclusive.toDouble()
                            )
                        }
                    }
                )

                PropertyDivider()

                // Row: Min/Max Rooms
                RangeSliderInput(
                    label = "Rooms",
                    value = (sp.minNbRooms?.toFloat() ?: 2f)..(sp.maxNbRooms?.toFloat() ?: 5f),
                    minValue = 0f,
                    maxValue = 10f,
                    steps = 10,
                    onValueChange = { range ->
                        viewModel.updateSearchProperties {
                            copy(
                                minNbRooms = range.start.toInt(),
                                maxNbRooms = range.endInclusive.toInt()
                            )
                        }
                    }
                )

                PropertyDivider()

                Button(
                    onClick = { viewModel.searchProperties() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // ---------- UI State Handling ----------
        when (uiState) {
            is SearchPropertiesUIState.WaitingForUserInteraction -> item { Text("") }
            is SearchPropertiesUIState.IsLoading -> item { LoadingScreen()  }
            is SearchPropertiesUIState.Error -> item { Text("Error: ${(uiState as SearchPropertiesUIState.Error).sError}") }
            is SearchPropertiesUIState.Success -> {
                val listProperties = (uiState as SearchPropertiesUIState.Success).listProperties
                items(listProperties) { property ->
                    PropertyListItem(
                        property = property,
                        onPropertyEditClick = { navController.navigate("detailed_property/${property.id}") },
                        onPropertyDeleteClick = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            else -> {}
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


@Composable
fun PropertyListItem(
    property: Property,
    onPropertyEditClick: (Long) -> Unit,
    onPropertyDeleteClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ---------- Column text ----------
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                // ---------- Property Price, surface, rooms ----------
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // Price
                    property.price?.let { price ->
                        Text(
                            text = "${price.toInt().formatWithSpaces()} €",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF388E3C)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Surface
                    property.surface?.let { surface ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SquareFoot,
                                contentDescription = "Surface",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${surface.toInt()} m²",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Rooms
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "Rooms",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${property.numbersOfRooms ?: 0} pièces",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // ---------- Column buttons ----------
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit button
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1976D2).copy(alpha = 0.15f),
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(onClick = { onPropertyEditClick(property.id!!) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Property",
                            tint = Color(0xFF1976D2)
                        )
                    }
                }

                // Delete button
                Surface(
                    shape = CircleShape,
                    color = Color.Red.copy(alpha = 0.15f),
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(onClick = { onPropertyDeleteClick(property.id!!) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Property",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(1.dp),
            color = Color.LightGray
        )
    }
}

