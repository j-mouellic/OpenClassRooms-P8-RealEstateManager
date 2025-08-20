package com.julien.mouellic.realestatemanager.ui.form.state

data class LocationFormState(
    val street: FieldState,
    val number: FieldState,
    val postalCode: FieldState,
    val city: FieldState,
    val country: FieldState,
    val longitude: FieldState,
    val latitude: FieldState
)
