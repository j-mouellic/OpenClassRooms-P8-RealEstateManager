package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.usecase.property.GetPropertyWithDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the detailed view of a property.
 *
 * Responsibilities:
 * 1. Expose a StateFlow of DetailedPropertyUIState to the UI.
 * 2. Load a property with all its details using the use case.
 * 3. Handle loading, success, and error states.
 *
 * @property getPropertyWithDetailsUseCase Use case to fetch a property with all details.
 */
@HiltViewModel
class DetailedPropertyViewModel @Inject constructor(
    private val getPropertyWithDetailsUseCase: GetPropertyWithDetailsUseCase
) : ViewModel() {

    // Backing StateFlow for UI observation
    private val _uiState = MutableStateFlow<DetailedPropertyUIState>(DetailedPropertyUIState.NoPropertySelected)
    val uiState: StateFlow<DetailedPropertyUIState> = _uiState

    /**
     * Load a property by its ID and update the UI state accordingly.
     *
     * Flow of states:
     * - Loading: indicates data fetching is in progress.
     * - Success: the property was found and is available to the UI.
     * - Error: an error occurred or property not found.
     *
     * @param propertyId ID of the property to load.
     */
    fun loadProperty(propertyId: Long) {
        viewModelScope.launch {
            try {
                // Show loading state to the UI
                _uiState.value = DetailedPropertyUIState.Loading

                // Fetch property using the use case
                val property = getPropertyWithDetailsUseCase(propertyId)

                // Update UI state depending on whether the property exists
                _uiState.value = if (property != null) {
                    DetailedPropertyUIState.Success(property)
                } else {
                    DetailedPropertyUIState.Error("Property not found")
                }
            } catch (e: Exception) {
                // Catch any unexpected errors and show in UI
                _uiState.value = DetailedPropertyUIState.Error(e.message)
            }
        }
    }
}
