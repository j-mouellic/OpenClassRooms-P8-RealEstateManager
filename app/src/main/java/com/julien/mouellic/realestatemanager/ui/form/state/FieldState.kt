package com.julien.mouellic.realestatemanager.ui.form.state

/**
 * FieldState
 *
 * Purpose:
 *  Represents the state of a single form field in the UI.
 *
 * Properties:
 *  - value: The current value of the field as a string.
 *  - isValid: Boolean indicating if the current value passes validation.
 *  - errorMessage: Optional error message to display if the field is invalid.
 *
 * Usage:
 *  This class is typically used with form validation logic and UI binding.
 *  The UI can observe the state to show the field value and display
 *  error messages dynamically.
 */
data class FieldState(
    val value: String,
    val isValid: Boolean,
    val errorMessage: String? = null
)
