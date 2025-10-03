package com.julien.mouellic.realestatemanager.ui.form.state

/**
 * LocationFormState
 *
 * Purpose:
 *  Represents the complete state of a location form in the UI.
 *  Each property is a FieldState, which tracks the current value, its validity, and any error message.
 *
 * Properties:
 *  - street: The street name field state.
 *  - number: The street number field state.
 *  - postalCode: The postal code field state.
 *  - city: The city field state.
 *  - country: The country field state.
 *  - longitude: The longitude field state (coordinates).
 *  - latitude: The latitude field state (coordinates).
 *
 * Usage:
 *  Used to manage form input for addresses.
 *  The UI can observe this state to display values and validation errors for each field.
 */
data class LocationFormState(
    val street: FieldState,
    val number: FieldState,
    val postalCode: FieldState,
    val city: FieldState,
    val country: FieldState,
    val longitude: FieldState,
    val latitude: FieldState
)
