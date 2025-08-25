package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.utils.CurrencyUtils

@Composable
fun DetailedPropertyScreen(
    propertyId: Long?,
    viewModel: DetailedPropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(propertyId) {
        propertyId?.let { viewModel.loadProperty(it) }
    }

    when (uiState) {
        is DetailedPropertyUIState.Loading -> {
            Text("Loading property...")
        }

        is DetailedPropertyUIState.Success -> {
            val property = (uiState as DetailedPropertyUIState.Success).property
            PropertyDetailContent(property = property)
        }

        is DetailedPropertyUIState.Error -> {
            Text("Error: ${(uiState as DetailedPropertyUIState.Error).message}")
        }
    }
}

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
        PropertyLocationSection(property)
        PropertyDescriptionSection(property)
        PropertyFeaturesSection(property)
        PropertyCommoditiesSection(property)
        PropertyAgentSection(property)
    }
}

@Composable
fun PropertyTitleSection(property: Property) {
    Text(
        text = property.name,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Price: " + CurrencyUtils.display(property.price),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

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

@Composable
fun PropertyLocationSection(property: Property) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Location:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    property.location?.let { loc ->
        val address = buildString {
            loc.streetNumber?.let { append(it).append(" ") }
            loc.street?.let { append(it).append(", ") }
            loc.postalCode?.let { append(it).append(" ") }
            loc.city?.let { append(it).append(", ") }
            loc.country?.let { append(it) }
        }.ifBlank { "-" }

        Text(address)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Divider()
}

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

fun Int.formatWithSpaces(): String {
    return this.toString().reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}
