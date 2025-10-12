package com.julien.mouellic.realestatemanager.ui.screen.createproperty

import com.google.android.gms.maps.model.LatLng
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.ui.form.state.FieldState
import com.julien.mouellic.realestatemanager.ui.form.state.InstantFieldState
import com.julien.mouellic.realestatemanager.ui.form.state.LocationFormState

/**
 * Represents the different UI states for the Create Property screen.
 *
 * This sealed class encapsulates all possible states that the UI can be in
 * during the creation or editing of a property.
 */
sealed class CreatePropertyUIState {

    /**
     * Indicates that the form is currently loading, for example when
     * fetching data from the repository or saving a property.
     *
     * @param formState The current state of the form while loading.
     */
    data class IsLoading(
        val formState: FormState
    ) : CreatePropertyUIState()

    /**
     * Indicates that the operation succeeded, e.g., the property was
     * successfully saved.
     *
     * @param propertyId The ID of the newly created or updated property.
     */
    data class Success(
        val propertyId: Long,
    ) : CreatePropertyUIState()

    /**
     * Represents an error state in the form.
     *
     * @param sError Optional error message describing what went wrong.
     * @param formState The current state of the form when the error occurred.
     */
    data class Error(
        val sError: String?,
        val formState: FormState
    ) : CreatePropertyUIState()

    /**
     * Represents the main state of the form containing all input values,
     * validation states, selected entities, and associated lists.
     *
     * @property propertyGPSLocation Optional GPS coordinates for the property.
     * @property name Current state of the name field.
     * @property description Current state of the description field.
     * @property surface Current state of the surface field.
     * @property nbRooms Current state of the number of rooms field.
     * @property nbBathrooms Current state of the number of bathrooms field.
     * @property nbBedrooms Current state of the number of bedrooms field.
     * @property price Current state of the price field.
     * @property entryDate Current state of the entry date field.
     * @property saleDate Current state of the sale date field.
     * @property apartmentNumber Current state of the apartment number field.
     * @property selectedAgent Currently selected agent for the property.
     * @property selectedEstateType Currently selected real estate type.
     * @property selectedCommodities List of commodities selected for the property.
     * @property pictures List of pictures associated with the property.
     * @property location Current state of the location fields.
     * @property isFormValid Indicates whether the form is currently valid.
     * @property allEstateTypes List of all available real estate types.
     * @property allCommodities List of all available commodities.
     * @property allAgents List of all available agents.
     */
    data class FormState(
        val propertyGPSLocation: LatLng? = null,
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
