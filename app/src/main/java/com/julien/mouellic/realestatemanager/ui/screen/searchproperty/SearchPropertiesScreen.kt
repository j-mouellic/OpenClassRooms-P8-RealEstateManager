package com.julien.mouellic.realestatemanager.ui.screen.searchproperty

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val searchProps = (uiState as? SearchPropertiesUIState.IsLoading)?.searchProperties
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
                Spacer(modifier = Modifier.height(16.dp))
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
        // TODO : ui state : not loading >>> wait for research
        when (uiState) {
            is SearchPropertiesUIState.IsLoading -> item { Text("Loading...") }
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
            // ---------- Column texte ----------
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = 16.dp)
            ) {
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

            // TODO : faire un component
            //  1. mettre des icons edit / delete
            //  2. remplacer view par un clic sur la card
            //  3. ajouter des champs de recherche



            // ---------- Column boutons ----------
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onPropertyEditClick(property.id!!) }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "View Property"
                    )
                }
                IconButton(onClick = { onPropertyDeleteClick(property.id!!) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Property"
                    )
                }
            }
        }
    }
}

