package com.julien.mouellic.realestatemanager.ui.screen.allproperties

import com.julien.mouellic.realestatemanager.domain.model.Property

sealed class AllPropertiesUIState {

    data class IsLoading(
        val searchProperties: SearchProperties
    ) : AllPropertiesUIState()

    data class Success(
        val listProperties : List<Property>,
        val searchProperties: SearchProperties
    ) : AllPropertiesUIState()

    data class Error(
        val sError: String?,
        val searchProperties: SearchProperties
    ) : AllPropertiesUIState()

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
    ) : AllPropertiesUIState()

}