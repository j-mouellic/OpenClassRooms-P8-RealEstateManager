package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import android.util.Log
import com.julien.mouellic.realestatemanager.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.utils.CurrencyUtils
import com.julien.mouellic.realestatemanager.utils.DateUtils

/**
 * DetailedPropertyScreen
 *
 * Composable that displays the detailed view of a single property.
 * It observes [DetailedPropertyViewModel.uiState] to reactively update
 * the UI when data is loaded, loading, or an error occurs.
 *
 * @param propertyId ID of the property to display. Can be null.
 * @param viewModel ViewModel providing property data and business logic.
 */
@Composable
fun DetailedPropertyScreen(
    propertyId: Long?,
    viewModel: DetailedPropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState() // Observe the UI state

    // Trigger property loading whenever propertyId changes
    LaunchedEffect(propertyId) {
        propertyId?.let { viewModel.loadProperty(it) }
    }

    // Render UI based on the current state
    when (uiState) {
        is DetailedPropertyUIState.NoPropertySelected -> {
            // Empty state UI
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "No property selected",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Select a property to see its details",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        is DetailedPropertyUIState.Loading -> {
            // Loading state UI
            Text("Loading property...")
        }

        is DetailedPropertyUIState.Success -> {
            // Display property details
            val property = (uiState as DetailedPropertyUIState.Success).property
            PropertyDetailContent(property = property)
        }

        is DetailedPropertyUIState.Error -> {
            // Error state UI
            Text("Error: ${(uiState as DetailedPropertyUIState.Error).message}")
        }
    }
}

/**
 * Displays the full detailed content of a property.
 *
 * Splits the property details into multiple sections:
 * - Title
 * - Images
 * - Dates
 * - Location
 * - Description
 * - Features
 * - Commodities/Environment
 * - Agent contact
 */
@Composable
fun PropertyDetailContent(property: Property) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        PropertyTitleSection(property)
        PropertyImagesSection(property)
        PropertyDateSection(property)
        PropertyLocationSection(property)
        PropertyDescriptionSection(property)
        PropertyFeaturesSection(property)
        PropertyCommoditiesSection(property)
        PropertyAgentSection(property)
    }
}

/**
 * Shows property name, price, and sale status.
 */
@Composable
fun PropertyTitleSection(property: Property) {
    Text(
        text = property.name,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Price : " + CurrencyUtils.display(property.price),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = if (property.isSold) "Sold" else "For Sale",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = if (property.isSold) Color.Red else Color(0xFF388E3C)
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays property images in a 2-column grid.
 */
@Composable
fun PropertyImagesSection(property: Property) {
    if (!property.pictures.isNullOrEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(property.pictures) { picture ->
                picture.content?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Property image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
    }
}

/**
 * Displays entry and sale dates of the property.
 */
@Composable
fun PropertyDateSection(property: Property) {
    val entryDate = property.entryDate?.let { DateUtils.format(it) } ?: "-"
    val saleDate = property.saleDate?.let { DateUtils.format(it) } ?: "-"

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Dates :",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Text("Entry Date : $entryDate", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Sale Date : $saleDate", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays property address and a static map.
 */
@Composable
fun PropertyLocationSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Location:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))

    // Build full address string
    val address = property.location?.let { loc ->
        buildString {
            loc.streetNumber?.let { append(it).append(" ") }
            loc.street?.let { append(it).append(", ") }
            loc.postalCode?.let { append(it).append(" ") }
            loc.city?.let { append(it).append(", ") }
            loc.country?.let { append(it) }
        }
    }.orEmpty()

    Text(if (address.isBlank()) "-" else address)

    if (address.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))

        // Display static Google Map for the address
        val encodedAddress = java.net.URLEncoder.encode(address, "UTF-8")
        val mapUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=$encodedAddress" +
                "&zoom=15" +
                "&size=600x300" +
                "&markers=color:red|$encodedAddress" +
                "&key=${BuildConfig.MAPS_API_KEY}"

        AsyncImage(
            model = mapUrl,
            contentDescription = "Property location map",
            modifier = Modifier.fillMaxWidth().height(180.dp),
            contentScale = ContentScale.Crop
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays the property description.
 */
@Composable
fun PropertyDescriptionSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Description:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(property.description ?: "-", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays key property features: surface, rooms, bedrooms, bathrooms.
 */
@Composable
fun PropertyFeaturesSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Property Features:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Column(modifier = Modifier.padding(start = 8.dp)) {
        Text("Surface: ${property.surface?.toInt() ?: 0} m²")
        Text("Rooms: ${property.numbersOfRooms ?: 0}")
        Text("Bedrooms: ${property.numbersOfBedrooms ?: 0}")
        Text("Bathrooms: ${property.numbersOfBathrooms ?: 0}")
    }
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays the property environment/commodities.
 */
@Composable
fun PropertyCommoditiesSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Environment & Commodities:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Column(modifier = Modifier.padding(start = 8.dp)) {
        property.commodities?.forEach { commodity ->
            Text("• ${commodity.name}")
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

/**
 * Displays the agent contact information.
 */
@Composable
fun PropertyAgentSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Contact:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    property.agent?.let { agent ->
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text("Name: ${agent.firstName} ${agent.lastName}")
            Text("Email: ${agent.email ?: "-"}")
            Text("Phone: ${agent.phoneNumber ?: "-"}")
        }
    }
}
