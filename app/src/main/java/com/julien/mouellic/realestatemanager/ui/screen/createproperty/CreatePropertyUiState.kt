package com.julien.mouellic.realestatemanager.ui.screen.createproperty

import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.ui.form.state.FieldState
import com.julien.mouellic.realestatemanager.ui.form.state.InstantFieldState
import com.julien.mouellic.realestatemanager.ui.form.state.LocationFormState

sealed class CreatePropertyUIState {

    data class IsLoading(
        val formState: FormState
    ) : CreatePropertyUIState()

    data class Success(
        val propertyId: Long
    ) : CreatePropertyUIState()

    data class Error(
        val sError: String?,
        val formState: FormState
    ) : CreatePropertyUIState()

    data class FormState(
        val name: FieldState,
        val description: FieldState,
        val surface: FieldState,
        val nbRooms: FieldState,
        val nbBathrooms: FieldState,
        val nbBedrooms: FieldState,
        val price: FieldState,
        val entryDate: InstantFieldState,
        val saleDate: InstantFieldState,
        val apartmentNumber: FieldState,
        val selectedAgent: Agent? = null,
        val selectedEstateType: RealEstateType? = null,
        val selectedCommodities: List<Commodity> = emptyList(),
        val pictures: List<Picture>,
        val location: LocationFormState,

        val isFormValid: Boolean,

        val allEstateTypes: List<RealEstateType> = emptyList(),
        val allCommodities: List<Commodity> = emptyList(),
        val allAgents: List<Agent> = emptyList()
    ) : CreatePropertyUIState()
}
