package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import com.julien.mouellic.realestatemanager.domain.model.Property

/**
 * Represents the UI state for the detailed property screen.
 *
 * The UI observes this state to render the property details screen correctly.
 * It covers all possible scenarios for this screen:
 *
 * States:
 * - [Loading]: Data is currently being fetched.
 * - [NoPropertySelected]: No property has been selected yet.
 * - [Success]: Property data has been successfully loaded and is available.
 * - [Error]: An error occurred while loading the property.
 */
sealed class DetailedPropertyUIState {

    /** Indicates that the property data is currently being loaded. */
    object Loading : DetailedPropertyUIState()

    /** Indicates that no property is currently selected to view. */
    object NoPropertySelected : DetailedPropertyUIState()

    /**
     * Indicates that the property data was successfully loaded.
     *
     * @property property The loaded property with all its details.
     */
    data class Success(val property: Property) : DetailedPropertyUIState()

    /**
     * Indicates that an error occurred while fetching the property.
     *
     * @property message Optional error message describing what went wrong.
     */
    data class Error(val message: String?) : DetailedPropertyUIState()
}
