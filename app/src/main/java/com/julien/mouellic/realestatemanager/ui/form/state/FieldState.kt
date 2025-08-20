package com.julien.mouellic.realestatemanager.ui.form.state

data class FieldState(
    val value: String,
    val isValid: Boolean,
    val errorMessage: String? = null
)
