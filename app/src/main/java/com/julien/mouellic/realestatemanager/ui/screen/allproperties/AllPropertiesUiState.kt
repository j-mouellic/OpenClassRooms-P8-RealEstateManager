package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import com.google.android.gms.maps.model.LatLng
import com.julien.mouellic.realestatemanager.domain.model.Property

/**
 * Represents the UI state for the "All Properties" screen.
 * This sealed class encapsulates all possible states that the UI can display,
 * including loading, success, error, and the current search/filter parameters.
 */
sealed class AllPropertiesUiState {

    /**
     * State representing that the properties are currently being loaded.
     *
     * @property searchProperties The current search/filter parameters being used.
     */
    data class IsLoading(
        val searchProperties: SearchProperties
    ) : AllPropertiesUiState()

    /**
     * State representing a successful data load.
     *
     * @property listProperties The list of properties returned by the search.
     * @property searchProperties The search/filter parameters used for this result.
     * @property propertyGPSLocation Optional current GPS location for map view.
     */
    data class Success(
        val listProperties : List<Property>,
        val searchProperties: SearchProperties,
        val propertyGPSLocation: LatLng? = null
    ) : AllPropertiesUiState()

    /**
     * State representing an error while loading properties.
     *
     * @property sError The error message, if available.
     * @property searchProperties The search/filter parameters used when the error occurred.
     */
    data class Error(
        val sError: String?,
        val searchProperties: SearchProperties
    ) : AllPropertiesUiState()

    /**
     * Represents the current search/filter parameters applied in the UI.
     *
     * @property type The ID of the selected real estate type (nullable).
     * @property minPrice Minimum price filter (nullable).
     * @property maxPrice Maximum price filter (nullable).
     * @property minSurface Minimum surface area filter (nullable).
     * @property maxSurface Maximum surface area filter (nullable).
     * @property minNbRooms Minimum number of rooms filter (nullable).
     * @property maxNbRooms Maximum number of rooms filter (nullable).
     * @property isAvailable Whether the property must be available (nullable).
     * @property commodities List of commodity IDs to filter by (nullable).
     */
    data class SearchProperties(
        val type: Long?,
        val minPrice: Double?,
        val maxPrice: Double?,
        val minSurface: Double?,
        val maxSurface: Double?,
        val minNbRooms: Int?,
        val maxNbRooms: Int?,
        val isAvailable: Boolean?,
        val commodities: List<Long>?
    ) : AllPropertiesUiState()
}
