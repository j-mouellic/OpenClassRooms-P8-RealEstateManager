package com.julien.mouellic.realestatemanager.ui.form.state

import org.threeten.bp.Instant

/**
 * InstantFieldState
 *
 * Purpose:
 *  Represents the state of a form field that holds a date/time value (Instant) in the UI.
 *
 * Properties:
 *  - value: The current Instant value of the field (nullable).
 *  - isValid: Boolean indicating if the current value passes validation.
 *  - errorMessage: Optional error message to display if the field is invalid.
 *
 * Usage:
 *  Used in forms where users input date/time values.
 *  The UI observes this state to display the selected date and any validation errors.
 */
data class InstantFieldState(
    val value: Instant?,
    val isValid: Boolean,
    val errorMessage: String? = null
)
