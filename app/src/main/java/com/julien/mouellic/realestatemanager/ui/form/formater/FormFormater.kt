package com.julien.mouellic.realestatemanager.ui.form.formater

import org.threeten.bp.Instant

/**
 * FormFormater
 *
 * Purpose:
 *  This class is part of the UI layer and is responsible for converting
 *  typed values (Int, Double, Boolean, Long, String) into strings suitable
 *  for display in forms or UI components.
 *
 *  Key behavior:
 *   - Null values are converted to empty strings ("")
 *   - Non-null values are converted using `toString()`
 *
 *  This ensures that form fields are always populated with valid strings,
 *  avoiding nulls in the UI and simplifying binding logic.
 */
class FormFormater {

    fun formatInt(value: Int?): String {
        return value?.toString() ?: ""
    }

    fun formatDouble(value: Double?): String {
        return value?.toString() ?: ""
    }

    fun formatBoolean(value: Boolean?): String {
        return value?.toString() ?: ""
    }

    fun formatLong(value: Long?): String {
        return value?.toString() ?: ""
    }

    fun formatString(value: String?): String {
        return value ?: ""
    }
}
