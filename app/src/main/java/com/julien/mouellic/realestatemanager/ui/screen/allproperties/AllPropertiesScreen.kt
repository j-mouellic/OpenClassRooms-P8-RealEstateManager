package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.julien.mouellic.realestatemanager.domain.model.Property

@Composable
fun AllPropertiesScreen(navController: NavHostController, viewModel: AllPropertiesViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableStateOf(0) }

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("List View")
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Map View")
            }
        }

        when (selectedTab) {
            0 -> {
                when (uiState) {
                    is AllPropertiesUIState.IsLoading -> {
                        Text("Loading...")
                    }
                    is AllPropertiesUIState.Success -> {
                        PropertyListView(properties = (uiState as AllPropertiesUIState.Success).listProperties, onPropertyEditClick = { propertyId ->
                            navController.navigate("edit_property/${propertyId}")
                        })
                    }
                    is AllPropertiesUIState.Error -> {
                        Text("Error: ${(uiState as AllPropertiesUIState.Error).sError}")
                    }
                    else -> {
                        Text("Error: Unknown state")
                    }
                }
            }
            1 -> PropertyMapView()
        }
    }
}

@Composable
fun PropertyListView(properties: List<Property>, onPropertyEditClick: (Long) -> Unit) {
    LazyColumn {
        items(properties) { property ->
            PropertyListItem(property = property, onPropertyEditClick = onPropertyEditClick)
        }
    }
}
@Composable
fun PropertyListItem(property: Property, onPropertyEditClick: (Long) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(0.70f)
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = property.id.toString())
                Text(text = property.description ?: "")
                property.location?.street?.let { Text(text = it) }
            }
        }

        Column(
            modifier = Modifier
                .weight(0.30f)
                .padding(8.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                }
            ) {
                Text(text = "Show")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onPropertyEditClick(property.id!!)
                }
            ) {
                Text(text = "Edit")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                }
            ) {
                Text(text = "Delete")
            }
        }
    }
}

@Composable
fun PropertyMapView() {
    Text("Map View - To be implemented with Google Maps")
}