package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.model.Property
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
    private val getAllPropertiesWithDetailsUseCase: GetAllPropertiesWithDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllPropertiesUIState>(AllPropertiesUIState.IsLoading)
    val uiState: StateFlow<AllPropertiesUIState> = _uiState

    init {
        getAllPropertiesWithDetails()
    }

    fun getAllPropertiesWithDetails() {
        _uiState.value = AllPropertiesUIState.IsLoading

        viewModelScope.launch {
            try {
                val properties: List<Property> = getAllPropertiesWithDetailsUseCase()
                _uiState.value = AllPropertiesUIState.Success(properties)
            } catch (e: Exception) {
                _uiState.value = AllPropertiesUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteProperty(propertyId: Long) {
        viewModelScope.launch {
            try {
                deletePropertyUseCase(propertyId)
                getAllPropertiesWithDetails()
            } catch (e: Exception) {
                _uiState.value = AllPropertiesUIState.Error(e.message ?: "Failed to delete")
            }
        }
    }
}