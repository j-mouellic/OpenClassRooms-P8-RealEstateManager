package com.julien.mouellic.realestatemanager.ui.screen.allproperties.list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Pageview
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.ui.component.LoadingScreen
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesUiState
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesViewModel
import com.julien.mouellic.realestatemanager.ui.screen.detailedproperty.DetailedPropertyScreen
import com.julien.mouellic.realestatemanager.utils.CurrencyUtils
import com.julien.mouellic.realestatemanager.utils.ResponsiveUtils

@Composable
fun AllPropertiesScreen(
    navController: NavHostController,
    viewModel: AllPropertiesViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }

    val uiState by viewModel.uiState.collectAsState()
    val isTablet = ResponsiveUtils.isTablet(LocalContext.current)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val tabTitles = listOf("List View", "Map View")
            val tabIcons = listOf(Icons.Default.List, Icons.Default.Map)

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF000000),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(4.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                        color = Color(0xFF000000),
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        selectedContentColor = Color(0xFF343434),
                        unselectedContentColor = Color.Gray
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    when (uiState) {
                        is AllPropertiesUiState.IsLoading -> {
                            LoadingScreen()
                        }

                        is AllPropertiesUiState.Success -> {
                            if (isTablet) {

                                var selectedPropertyId by remember { mutableStateOf<Long?>(null) }

                                Row(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        PropertyListView(
                                            properties = (uiState as AllPropertiesUiState.Success).listProperties,
                                            onPropertyEditClick = { propertyId ->
                                                navController.navigate("edit_property/$propertyId")
                                            },
                                            onPropertyShowClick = { propertyId ->
                                                selectedPropertyId = propertyId
                                            },
                                            onPropertyDeleteClick = { propertyId ->
                                                viewModel.deleteProperty(propertyId)
                                            }
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(Color(0xFFF8F8F8))
                                    ) {
                                        DetailedPropertyScreen(
                                            propertyId = selectedPropertyId,
                                        )
                                    }
                                }
                            } else {
                                PropertyListView(
                                    properties = (uiState as AllPropertiesUiState.Success).listProperties,
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
                        }

                        is AllPropertiesUiState.Error -> {
                            Text("Error: ${(uiState as AllPropertiesUiState.Error).sError}")
                        }

                        else -> {
                            Text("Error: Unknown state")
                        }
                    }
                }

                1 -> {
                    when (uiState) {
                        is AllPropertiesUiState.IsLoading -> {
                            LoadingScreen()
                        }

                        is AllPropertiesUiState.Success -> {
                            PropertyMapView(
                                properties = (uiState as AllPropertiesUiState.Success).listProperties,
                                onPropertyShowClick = { propertyId ->
                                    navController.navigate("detailed_property/$propertyId")
                                })
                        }

                        is AllPropertiesUiState.Error -> {
                            Text("Error: ${(uiState as AllPropertiesUiState.Error).sError}")
                        }

                        else -> {
                            Text("Error: Unknown state")
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                viewModel.updateSearchProperties(
                    AllPropertiesUiState.SearchProperties(
                        type = null,
                        minPrice = null,
                        maxPrice = null,
                        minSurface = null,
                        maxSurface = null,
                        minNbRooms = null,
                        maxNbRooms = null,
                        isAvailable = null,
                        commodities = null
                    )
                )
                viewModel.searchProperties()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterAltOff,
                contentDescription = "Reset filters"
            )
        }
    }
}


@Composable
fun PropertyListView(
    properties: List<Property>,
    onPropertyEditClick: (Long) -> Unit,
    onPropertyShowClick: (Long) -> Unit,
    onPropertyDeleteClick: (Long) -> Unit
) {
    LazyColumn {
        items(properties) { property ->
            PropertyListItem(
                property = property,
                onPropertyEditClick = onPropertyEditClick,
                onPropertyShowClick = onPropertyShowClick,
                onPropertyDeleteClick = onPropertyDeleteClick
            )
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
        // ---------- Property Card ----------
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.85f)
                .padding(8.dp)
                .clickable { onPropertyShowClick(property.id!!) },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // ---------- Property Image ----------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val firstImage = property.pictures.firstOrNull()?.content?.asImageBitmap()

                    if (firstImage != null) {
                        Image(
                            bitmap = firstImage,
                            contentDescription = "Property Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        )
                    } else {
                        // Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Image",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ---------- Property Name and Status ----------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = property.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = if (property.isSold) "Sold" else "For Sale",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = if (property.isSold) Color.Red else Color(0xFF388E3C)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ---------- Property Price, surface, rooms ----------
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // Price
                    property.price?.let { price ->
                        Text(
                            text = "Price : " + CurrencyUtils.display(price),
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

                // ---------- Property Description ----------
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

        // Show, Edit, delete icon buttons
        Column(
            modifier = Modifier
                .weight(0.15f)
                .padding(8.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Show button
            Surface(
                shape = CircleShape,
                color = Color(0xFF5A5A5D).copy(alpha = 0.15f),
                shadowElevation = 4.dp,
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(onClick = { onPropertyShowClick(property.id!!) }) {
                    Icon(
                        imageVector = Icons.Default.Pageview,
                        contentDescription = "Show Property",
                        tint = Color(0xFF5A5A5D)
                    )
                }
            }

            // Edit button
            Surface(
                shape = CircleShape,
                color = Color(0xFF1976D2).copy(alpha = 0.15f),
                shadowElevation = 4.dp,
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
                shadowElevation = 4.dp,
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
    Divider(
        color = Color.Gray.copy(alpha = 0.5f),
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
fun PropertyMapView(
    properties: List<Property> = emptyList(),
    onPropertyShowClick: (Long) -> Unit = {}
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.8566, 2.3522), 16f) // Default : Paris
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
                    onPropertyShowClick(property.id!!)
                    true
                }
            )
        }
    }
}

