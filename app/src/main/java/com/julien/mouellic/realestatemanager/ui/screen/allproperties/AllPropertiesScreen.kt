package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
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
                        PropertyListView(
                            properties = (uiState as AllPropertiesUIState.Success).listProperties,
                            onPropertyEditClick = { propertyId ->
                                navController.navigate("edit_property/$propertyId")
                            },
                            onPropertyShowClick = { propertyId ->
                                navController.navigate("detailed_property/$propertyId")
                            },
                            onPropertyDeleteClick = { propertyId ->
                                viewModel.deleteProperty(propertyId)
                            }
                        )
                    }
                    is AllPropertiesUIState.Error -> {
                        Text("Error: ${(uiState as AllPropertiesUIState.Error).sError}")
                    }
                    else -> {
                        Text("Error: Unknown state")
                    }
                }
            }
            1 -> {
                when (uiState) {
                    is AllPropertiesUIState.Success -> {
                        PropertyMapView(properties = (uiState as AllPropertiesUIState.Success).listProperties, onPropertyEditClick = { propertyId ->
                            navController.navigate("edit_property/$propertyId")
                        })
                    }
                    else -> {
                        viewModel.getPropertyWithDetailsForMap()
                        Text("Loading Map...")
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyListView(
    properties: List<Property>,
    onPropertyEditClick: (Long) -> Unit,
    onPropertyShowClick: (Long) -> Unit,
    onPropertyDeleteClick: (Long) -> Unit) {
    LazyColumn {
        items(properties) { property ->
            PropertyListItem(
                property = property,
                onPropertyEditClick = onPropertyEditClick,
                onPropertyShowClick = onPropertyShowClick,
                onPropertyDeleteClick = onPropertyDeleteClick)
        }
    }
}


@Composable
fun PropertyListItem(
    property: Property,
    onPropertyEditClick: (Long) -> Unit,
    onPropertyShowClick: (Long) -> Unit,
    onPropertyDeleteClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp)
    ) {
        // Clickable Card >>> Show detailed property
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.85f)
                .padding(8.dp)
                .clickable { onPropertyShowClick(property.id!!) },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Nom
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Ligne price, surface, rooms
                Row(
                    verticalAlignment = Alignment.CenterVertically,
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

                // Description
                property.description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Delete & Edit icon button
        Column(
            modifier = Modifier
                .weight(0.15f)
                .padding(8.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onPropertyEditClick(property.id!!) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Property",
                    tint = Color(0xFF1976D2)
                )
            }

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

/*@Composable
fun PropertyListItem(
    property: Property,
    onPropertyEditClick: (Long) -> Unit,
    onPropertyShowClick: (Long) -> Unit,
    onPropertyDeleteClick: (Long) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.70f)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Name
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
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

                    // Icon + Surface
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

                    Spacer(modifier = Modifier.width(16.dp)) // espace entre surface et rooms

                    // Icon + Number of Rooms
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

                // Description
                property.description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
                    onPropertyShowClick(property.id!!)
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
                    onPropertyDeleteClick(property.id!!)
                }
            ) {
                Text(text = "Delete")
            }
        }
    }
}*/


@Composable
fun PropertyMapView(properties: List<Property> = emptyList(), onPropertyEditClick: (Long) -> Unit = {}) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.8566, 2.3522), 12f) // Paris par défaut
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
       properties.forEach { property ->
            val lat = property.location?.latitude ?: return@forEach
            val lng = property.location?.longitude ?: return@forEach

           if (lat == null || lng == null) {
               Log.w("PropertyMapView", "Property ${property.id} has no location!")
               return@forEach
           }

           Log.d("PropertyMapView", "Adding marker for property ${property.id} at ($lat, $lng)")

            Marker(
                state = MarkerState(position = LatLng(lat, lng)),
                title = property.description ?: "Property ${property.id}",
                snippet = property.location.street ?: "",
                onClick = {
                    Log.d("PropertyMapView", "Marker clicked for property ${property.id}")
                    onPropertyEditClick(property.id!!)
                    true
                }
            )
        }
    }
}

fun Int.formatWithSpaces(): String {
    return this.toString().reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}
