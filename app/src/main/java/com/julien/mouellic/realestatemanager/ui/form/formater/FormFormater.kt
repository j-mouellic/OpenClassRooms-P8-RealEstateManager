package com.julien.mouellic.realestatemanager.ui.form.formater

import org.threeten.bp.Instant

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