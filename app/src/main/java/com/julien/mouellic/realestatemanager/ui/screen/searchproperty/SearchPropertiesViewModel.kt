package com.julien.mouellic.realestatemanager.ui.screen.searchproperty

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import com.julien.mouellic.realestatemanager.domain.usecase.commodity.GetAllCommoditiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.SearchPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchPropertiesVM"

@HiltViewModel
class SearchPropertiesViewModel @Inject constructor(
    private val searchPropertiesUseCase: SearchPropertiesUseCase,
    private val getAllCommoditiesUseCase: GetAllCommoditiesUseCase
) : ViewModel() {

    // Properties
    private val _uiState = MutableStateFlow<SearchPropertiesUIState>(
        SearchPropertiesUIState.WaitingForUserInteraction(SearchPropertiesUIState.SearchProperties())
    )
    val uiState: StateFlow<SearchPropertiesUIState> = _uiState

    // Commodities
    private val _commodities = MutableStateFlow<List<Commodity>>(emptyList())
    val commodities: StateFlow<List<Commodity>> = _commodities

    // Init Get Commodities
    init {
        viewModelScope.launch {
            try {
                val result = getAllCommoditiesUseCase()
                if (result.isSuccess) {
                    _commodities.value = result.getOrNull() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("Commodity", "Error fetching commodities", e)
            }
        }
    }

    private fun getSearchProperties(): SearchPropertiesUIState.SearchProperties {
        return when (val uiState = _uiState.value) {
            is SearchPropertiesUIState.IsLoading -> uiState.searchProperties
            is SearchPropertiesUIState.Success -> uiState.searchProperties
            is SearchPropertiesUIState.Error -> uiState.searchProperties
            is SearchPropertiesUIState.WaitingForUserInteraction -> uiState.searchProperties
        }
    }

    fun searchProperties() {
        val searchProperties = getSearchProperties()
        Log.d(TAG, "searchProperties() called with filters: $searchProperties")

        _uiState.value = SearchPropertiesUIState.IsLoading(searchProperties)

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
                Log.d(TAG, "Found ${properties.size} properties")
                _uiState.value = SearchPropertiesUIState.Success(properties, searchProperties)
            } catch (exception: Exception) {
                Log.e(TAG, "Error fetching properties: ${exception.message}", exception)
                _uiState.value = SearchPropertiesUIState.Error(exception.message, searchProperties)
            }
        }
    }

    fun updateSearchProperties(update: SearchPropertiesUIState.SearchProperties.() -> SearchPropertiesUIState.SearchProperties) {
        val current = getSearchProperties()
        val updated = current.update()
        Log.d(TAG, "updateSearchProperties() called, new filters: $updated")
        _uiState.value = SearchPropertiesUIState.WaitingForUserInteraction(updated)
    }
}
