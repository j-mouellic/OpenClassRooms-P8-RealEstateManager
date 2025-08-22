package com.julien.mouellic.realestatemanager.ui.screen.detailedproperty

import com.julien.mouellic.realestatemanager.domain.model.Property

sealed class DetailedPropertyUIState {
    object Loading : DetailedPropertyUIState()
    data class Success(val property: Property) : DetailedPropertyUIState()
    data class Error(val message: String?) : DetailedPropertyUIState()
}