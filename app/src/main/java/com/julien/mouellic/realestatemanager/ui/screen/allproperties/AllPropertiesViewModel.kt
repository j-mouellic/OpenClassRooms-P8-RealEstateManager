package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.julien.mouellic.realestatemanager.data.repository.GPSRepository
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import com.julien.mouellic.realestatemanager.domain.usecase.commodity.GetAllCommoditiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.DeletePropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.SearchPropertiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.realestatetype.GetAllEstateTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for the "All Properties" screen && "Search Properties" screen
 *
 * Handles both the main properties listing (list/map view) and the search/filter functionality.
 * It communicates with use cases to fetch properties, estate types, and commodities, and exposes
 * the UI state as a [StateFlow] for the Compose UI to observe.
 *
 * Responsibilities include:
 * - Loading all properties according to current search/filter parameters.
 * - Loading all estate types and commodities for the search view filters.
 * - Handling deletion of properties.
 * - Updating the user's GPS location for map views.
 */
@HiltViewModel
class AllPropertiesViewModel @Inject constructor(
    private val searchPropertiesUseCase: SearchPropertiesUseCase,
    private val getAllCommoditiesUseCase: GetAllCommoditiesUseCase,
    private val getAllEstateTypesUseCase: GetAllEstateTypesUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val gpsRepository: GPSRepository
) : ViewModel() {

    /**
     * The main UI state observed by the Compose UI.
     * Can be Loading, Success, Error, or holding current search/filter parameters.
     */
    private val _uiState = MutableStateFlow<AllPropertiesUiState>(
        AllPropertiesUiState.IsLoading(
            AllPropertiesUiState.SearchProperties(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )
    )
    val uiState: StateFlow<AllPropertiesUiState> = _uiState

    /** List of all available real estate types for the search filter. */
    private val _allTypes = MutableStateFlow<List<RealEstateType>>(emptyList())
    val allTypes: StateFlow<List<RealEstateType>> = _allTypes

    /** List of all available commodities for the search filter. */
    private val _allCommodities = MutableStateFlow<List<Commodity>>(emptyList())
    val allCommodities: StateFlow<List<Commodity>> = _allCommodities

    init {
        // Initialize main properties list and GPS updates
        searchProperties()
        startLocationUpdates()

        // Load data for search/filter view
        loadTypes()
        loadCommodities()
    }

    /**
     * Loads all estate types from the domain layer for the search filters.
     * Updates [_allTypes] state flow.
     */
    private fun loadTypes() {
        viewModelScope.launch {
            try {
                val result = getAllEstateTypesUseCase()
                result.onSuccess { types ->
                    _allTypes.value = types
                    println("🔹 Loaded ${types.size} estate types")
                }.onFailure { e ->
                    println("🔹 Failed to load estate types: ${e.message}")
                }
            } catch (e: Exception) {
                println("🔹 Exception loading estate types: ${e.message}")
            }
        }
    }

    /**
     * Loads all commodities from the domain layer for the search filters.
     * Updates [_allCommodities] state flow.
     */
    private fun loadCommodities() {
        viewModelScope.launch {
            try {
                val result = getAllCommoditiesUseCase()
                result.onSuccess { commodities ->
                    _allCommodities.value = commodities
                    println("🔹 Loaded ${commodities.size} commodities")
                }.onFailure { e ->
                    println("🔹 Failed to load commodities: ${e.message}")
                }
            } catch (e: Exception) {
                println("🔹 Exception loading commodities: ${e.message}")
            }
        }
    }

    /**
     * Starts GPS location updates by collecting from the [GPSRepository].
     * Updates the propertyGPSLocation in [AllPropertiesUiState.Success] when available.
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        viewModelScope.launch {
            gpsRepository.getLocationUpdate().collect{
                    location ->
                updateLocation(location)
            }
        }
    }

    /**
     * Updates the current GPS location in the UI state.
     * @param location The latest location from GPS.
     */
    private fun updateLocation(location: android.location.Location) {
        when(val currentState = _uiState.value){
            is AllPropertiesUiState.Success -> {
                val newLocation = LatLng(location.latitude, location.longitude)
                _uiState.value = currentState.copy(
                    propertyGPSLocation = newLocation
                )
                println("🔹 GPS updated in Success state: $newLocation")
            }
            is AllPropertiesUiState.Error -> {}
            is AllPropertiesUiState.IsLoading -> {}
            is AllPropertiesUiState.SearchProperties -> {}
        }
    }

    /**
     * Retrieves the current search/filter parameters from the UI state.
     */
    private fun getSearchProperties(): AllPropertiesUiState.SearchProperties {
        val searchProps = when (val uiState = _uiState.value) {
            is AllPropertiesUiState.IsLoading -> uiState.searchProperties
            is AllPropertiesUiState.Success -> uiState.searchProperties
            is AllPropertiesUiState.Error -> uiState.searchProperties
            is AllPropertiesUiState.SearchProperties -> uiState
        }
        println("🔹 getSearchProperties returned: $searchProps")
        return searchProps
    }

    /**
     * Updates the search/filter parameters in the UI state.
     * @param newSearchProperties The new search criteria to apply.
     */
    fun updateSearchProperties(newSearchProperties: AllPropertiesUiState.SearchProperties) {
        println("🔹 updateSearchProperties called with: $newSearchProperties")
        _uiState.value = when (val currentState = _uiState.value) {
            is AllPropertiesUiState.IsLoading -> currentState.copy(searchProperties = newSearchProperties)
            is AllPropertiesUiState.Success -> currentState.copy(searchProperties = newSearchProperties)
            is AllPropertiesUiState.Error -> currentState.copy(searchProperties = newSearchProperties)
            is AllPropertiesUiState.SearchProperties -> newSearchProperties
        }
        println("🔹 uiState after update: ${_uiState.value}")
    }

    /**
     * Executes a search for properties based on current search/filter parameters.
     * Updates [_uiState] to Loading, then Success or Error depending on the result.
     */
    fun searchProperties() {
        val searchProperties = getSearchProperties()
        println("🔹 searchProperties() called with: $searchProperties")

        _uiState.value = AllPropertiesUiState.IsLoading(searchProperties)

        viewModelScope.launch {
            searchPropertiesUseCase(
                type = searchProperties.type,
                minPrice = searchProperties.minPrice,
                maxPrice = searchProperties.maxPrice,
                minSurface = searchProperties.minSurface,
                maxSurface = searchProperties.maxSurface,
                minNbRooms = searchProperties.minNbRooms,
                maxNbRooms = searchProperties.maxNbRooms,
                isAvailable = searchProperties.isAvailable,
                commodities = searchProperties.commodities
            ).collect { properties ->
                println("🔹 Flow emitted ${properties.size} properties")
                _uiState.value = AllPropertiesUiState.Success(properties, searchProperties)
            }
        }
    }

    /**
     * Deletes a property by ID and refreshes the properties list.
     * @param propertyId The ID of the property to delete.
     */
    fun deleteProperty(propertyId: Long) {
        viewModelScope.launch {
            deletePropertyUseCase(propertyId)
        }
    }
}