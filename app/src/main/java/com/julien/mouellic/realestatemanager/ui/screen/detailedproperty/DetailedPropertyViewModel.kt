package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.usecase.property.GetPropertyWithDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedPropertyViewModel @Inject constructor(
    private val getPropertyWithDetailsUseCase: GetPropertyWithDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailedPropertyUIState>(DetailedPropertyUIState.NoPropertySelected)
    val uiState: StateFlow<DetailedPropertyUIState> = _uiState

    fun loadProperty(propertyId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = DetailedPropertyUIState.Loading
                val property = getPropertyWithDetailsUseCase(propertyId)
                if (property != null) {
                    _uiState.value = DetailedPropertyUIState.Success(property)
                } else {
                    _uiState.value = DetailedPropertyUIState.Error("Property not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailedPropertyUIState.Error(e.message)
            }
        }
    }
}