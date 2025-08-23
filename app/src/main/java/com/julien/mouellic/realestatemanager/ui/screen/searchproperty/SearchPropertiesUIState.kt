package com.julien.mouellic.realestatemanager.ui.screen.searchproperty

import com.julien.mouellic.realestatemanager.domain.model.Property

sealed class SearchPropertiesUIState {

    data class WaitingForUserInteraction(
        val searchProperties: SearchProperties
    ) : SearchPropertiesUIState()

    data class IsLoading(
        val searchProperties: SearchProperties
    ) : SearchPropertiesUIState()

    data class Success(
        val listProperties : List<Property>,
        val searchProperties: SearchProperties
    ) : SearchPropertiesUIState()

    data class Error(
        val sError: String?,
        val searchProperties: SearchProperties
    ) : SearchPropertiesUIState()

    data class SearchProperties(
        val type: Long? = null,
        val minPrice: Double? = null,
        val maxPrice: Double? = null,
        val minSurface: Double? = null,
        val maxSurface: Double? = null,
        val minNbRooms: Int? = null,
        val maxNbRooms: Int? = null,
        val isAvailable: Boolean? = null,
        val commodities: List<Long>? = null
    )
}
