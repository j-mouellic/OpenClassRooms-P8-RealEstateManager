package com.julien.mouellic.realestatemanager.ui.screen.allproperties


import com.julien.mouellic.realestatemanager.domain.model.Property

sealed class AllPropertiesUIState {
    object IsLoading : AllPropertiesUIState()
    data class Success(val listProperties: List<Property>) : AllPropertiesUIState()
    data class Error(val errorMessage: String) : AllPropertiesUIState()
}
