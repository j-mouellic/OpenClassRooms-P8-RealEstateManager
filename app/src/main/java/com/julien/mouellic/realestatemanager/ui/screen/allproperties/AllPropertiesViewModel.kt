package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class AllPropertiesViewModel @Inject constructor(
    private val searchPropertiesUseCase: SearchPropertiesUseCase,
    private val getAllCommoditiesUseCase: GetAllCommoditiesUseCase,
    private val getAllEstateTypesUseCase: GetAllEstateTypesUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase
) : ViewModel() {

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

    private val _allTypes = MutableStateFlow<List<RealEstateType>>(emptyList())
    val allTypes: StateFlow<List<RealEstateType>> = _allTypes

    private val _allCommodities = MutableStateFlow<List<Commodity>>(emptyList())
    val allCommodities: StateFlow<List<Commodity>> = _allCommodities

    init {
        searchProperties()
        loadTypes()
        loadCommodities()
    }

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

    fun searchProperties() {
        val searchProperties = getSearchProperties()
        println("🔹 searchProperties() called with: $searchProperties")

        _uiState.value = AllPropertiesUiState.IsLoading(searchProperties)

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
                println("🔹 searchPropertiesUseCase returned ${properties.size} properties")

                _uiState.value = AllPropertiesUiState.Success(properties, searchProperties)
                println("🔹 uiState updated to Success with ${properties.size} properties")
            } catch (exception: Exception) {
                println("🔹 searchProperties failed: ${exception.message}")
                _uiState.value = AllPropertiesUiState.Error(exception.message, searchProperties)
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