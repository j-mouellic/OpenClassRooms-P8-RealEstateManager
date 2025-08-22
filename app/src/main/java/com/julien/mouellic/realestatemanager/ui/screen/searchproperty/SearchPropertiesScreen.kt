package com.julien.mouellic.realestatemanager.ui.screen.searchproperty

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.julien.mouellic.realestatemanager.domain.model.Property

@Composable
fun SearchPropertiesScreen(
    navController: NavHostController,
    viewModel: SearchPropertiesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Text("Search Properties", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        val searchProps = (uiState as? SearchPropertiesUIState.IsLoading)?.searchProperties
            ?: (uiState as? SearchPropertiesUIState.Success)?.searchProperties
            ?: (uiState as? SearchPropertiesUIState.Error)?.searchProperties

        searchProps?.let { sp ->
            OutlinedTextField(
                value = sp.minPrice?.toString() ?: "",
                onValueChange = { value ->
                    viewModel.updateSearchProperties { copy(minPrice = value.toDoubleOrNull()) }
                },
                label = { Text("Min Price") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = sp.maxPrice?.toString() ?: "",
                onValueChange = { value ->
                    viewModel.updateSearchProperties { copy(maxPrice = value.toDoubleOrNull()) }
                },
                label = { Text("Max Price") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.searchProperties() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is SearchPropertiesUIState.IsLoading -> Text("Loading...")
            is SearchPropertiesUIState.Error -> Text("Error: ${(uiState as SearchPropertiesUIState.Error).sError}")
            is SearchPropertiesUIState.Success -> {
                val listProperties = (uiState as SearchPropertiesUIState.Success).listProperties
                LazyColumn {
                    items(listProperties) { property ->
                        PropertyListItem(
                            property = property,
                            onPropertyEditClick = { propertyId ->
                                navController.navigate("detailed_property/$propertyId")
                            },
                            onPropertyDeleteClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            else -> {}
        }
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
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                property.price?.let { price ->
                    Text(
                        text = "${price.toInt()} €",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF388E3C)
                    )
                }
                property.surface?.let { surface ->
                    Text(
                        text = "${surface.toInt()} m²",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Button(
                    onClick = { onPropertyEditClick(property.id!!) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onPropertyDeleteClick(property.id!!) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete")
                }
            }
        }
    }
}


