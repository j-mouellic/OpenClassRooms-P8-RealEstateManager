package com.julien.mouellic.realestatemanager.ui.form.validator

import com.julien.mouellic.realestatemanager.ui.form.state.FieldState
import com.julien.mouellic.realestatemanager.ui.form.state.InstantFieldState
import org.threeten.bp.Instant

/**
 * FormValidator
 *
 * Purpose:
 *  Centralizes form validation logic for different types of fields.
 *  Ensures that input values respect required constraints (mandatory, range, format, length).
 *
 * Methods:
 *  - validateInstant: Validates date/time inputs.
 *  - validateInteger: Validates integer string inputs.
 *  - validateDouble: Validates double string inputs.
 *  - validateString: Validates text string inputs.
 *
 * Returns:
 *  - FieldState or InstantFieldState for each input, which contains:
 *      - value: the original input
 *      - isValid: whether the input passes validation
 *      - errorMessage: optional message to display if invalid
 */
class FormValidator {

    /**
     * Validates an Instant (date/time) field.
     * Checks if the value is mandatory, and if it lies within optional min/max bounds.
     */
    fun validateInstant(
        instant: Instant?,
        minInstant: Instant?,
        maxInstant: Instant?,
        mandatory: Boolean
    ): InstantFieldState {
        return if (instant == null) {
            if (mandatory) InstantFieldState(null, false, "Cannot be empty")
            else InstantFieldState(null, true)
        } else if (minInstant != null && instant.isBefore(minInstant)) {
            InstantFieldState(instant, false, "Must be at least $minInstant")
        } else if (maxInstant != null && instant.isAfter(maxInstant)) {
            InstantFieldState(instant, false, "Must be at most $maxInstant")
        } else {
            InstantFieldState(instant, true)
        }
    }

    /**
     * Validates an integer field represented as a string.
     * Checks mandatory, numeric format, and optional min/max constraints.
     */
    fun validateInteger(
        value: String,
        minIfNotBlank: Int?,
        maxIfNotBlank: Int?,
        mandatory: Boolean
    ): FieldState {
        return if (value.isBlank()) {
            if (mandatory) FieldState(value, false, "Cannot be empty")
            else FieldState(value, true)
        } else if (value.toIntOrNull() == null) {
            FieldState(value, false, "Must be a number")
        } else if (minIfNotBlank != null && value.toInt() < minIfNotBlank) {
            FieldState(value, false, "Must be at least $minIfNotBlank")
        } else if (maxIfNotBlank != null && value.toInt() > maxIfNotBlank) {
            FieldState(value, false, "Must be at most $maxIfNotBlank")
        } else {
            FieldState(value, true)
        }
    }

    /**
     * Validates a double field represented as a string.
     * Checks mandatory, numeric format, and optional min/max constraints.
     */
    fun validateDouble(
        value: String,
        minIfNotBlank: Double?,
        maxIfNotBlank: Double?,
        mandatory: Boolean
    ): FieldState {
        return if (value.isBlank()) {
            if (mandatory) FieldState(value, false, "Cannot be empty")
            else FieldState(value, true)
        } else if (value.toDoubleOrNull() == null) {
            FieldState(value, false, "Must be a number")
        } else if (minIfNotBlank != null && value.toDouble() < minIfNotBlank) {
            FieldState(value, false, "Must be at least $minIfNotBlank")
        } else if (maxIfNotBlank !== null && value.toDouble() > maxIfNotBlank) {
            FieldState(value, false, "Must be at most $maxIfNotBlank")
        } else {
            FieldState(value, true)
        }
    }

    /**
     * Validates a string field.
     * Checks mandatory and optional min/max length constraints.
     */
    fun validateString(
        value: String,
        minLength: Int?,
        maxLength: Int?,
        mandatory: Boolean
    ): FieldState {
        return if (value.isBlank()) {
            if (mandatory) FieldState(value, false, "Cannot be empty")
            else FieldState(value, true)
        } else if (minLength != null && value.length < minLength) {
            FieldState(value, false, "Must be at least $minLength characters")
        } else if (maxLength != null && value.length > maxLength) {
            FieldState(value, false, "Must be at most $maxLength characters")
        } else {
            FieldState(value, true)
        }
    }
}
