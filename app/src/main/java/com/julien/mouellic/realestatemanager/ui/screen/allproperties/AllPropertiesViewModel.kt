package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.usecase.property.DeletePropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.GetAllPropertiesWithDetailsUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.SearchPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPropertiesViewModel @Inject constructor(
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val searchPropertiesUseCase: SearchPropertiesUseCase,
    private val getAllPropertiesWithDetailsUseCase: GetAllPropertiesWithDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllPropertiesUIState>(AllPropertiesUIState.IsLoading(
        AllPropertiesUIState.SearchProperties(
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
    ))
    val uiState: StateFlow<AllPropertiesUIState> = _uiState

    init {
        searchProperties()
    }

    private fun getSearchProperties(): AllPropertiesUIState.SearchProperties {
        return when (val uiState = _uiState.value) {
            is AllPropertiesUIState.IsLoading -> uiState.searchProperties
            is AllPropertiesUIState.Success -> uiState.searchProperties
            is AllPropertiesUIState.Error -> uiState.searchProperties
            is AllPropertiesUIState.SearchProperties -> uiState
        }
    }

    fun searchProperties() {
        val searchProperties = getSearchProperties()

        _uiState.value = AllPropertiesUIState.IsLoading(searchProperties)

        viewModelScope.launch {
            try {
                val properties = searchPropertiesUseCase(
                    type = searchProperties.type,
                    minPrice = searchProperties.minPrice,
                    maxPrice = searchProperties.maxPrice,
                    minSurface = searchProperties.minSurface,
                    maxSurface = searchProperties.maxSurface,
                    minNbRooms = searchProperties.minNbRooms,
                    maxNbRooms = searchProperties.maxNbRooms,
                    isAvailable = searchProperties.isAvailable,
                    commodities = searchProperties.commodities
                )

                _uiState.value = AllPropertiesUIState.Success(properties, searchProperties)
            } catch (exception: Exception) {
                _uiState.value = AllPropertiesUIState.Error(exception.message, searchProperties)
            }
        }
    }

    fun getPropertyWithDetailsForMap() {
        val searchProperties = getSearchProperties()
        _uiState.value = AllPropertiesUIState.IsLoading(searchProperties)

        viewModelScope.launch {
            try {
                val properties = getAllPropertiesWithDetailsUseCase()
                _uiState.value = AllPropertiesUIState.Success(properties, searchProperties)
            } catch (exception: Exception) {
                _uiState.value = AllPropertiesUIState.Error(exception.message, searchProperties)
            }
        }
    }

    fun deleteProperty(propertyId: Long) {
        viewModelScope.launch {
            deletePropertyUseCase(propertyId)
            searchProperties()
        }
    }
}