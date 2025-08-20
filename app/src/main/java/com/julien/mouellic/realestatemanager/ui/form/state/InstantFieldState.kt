package com.julien.mouellic.realestatemanager.ui.form.state

import org.threeten.bp.Instant

data class InstantFieldState(
    val value: Instant?,
    val isValid: Boolean,
    val errorMessage: String? = null
)
