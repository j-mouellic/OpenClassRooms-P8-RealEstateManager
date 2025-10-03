package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.ui.form.converter.FormConverter
import com.julien.mouellic.realestatemanager.ui.form.formater.FormFormater
import com.julien.mouellic.realestatemanager.ui.form.validator.FormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * FormModule provides dependencies related to form processing, including:
 * - Validation
 * - Conversion
 * - Formatting
 *
 * By using Hilt for Dependency Injection (DI):
 * 1. Separation of Concerns (SOC): ViewModels or UI components do not need to know how to create
 *    these utilities.
 * 2. SingletonComponent scope allows reusing these instances across the application if needed.
 * 3. Simplifies testing by allowing mocks to replace these dependencies.
 */
@Module
@InstallIn(SingletonComponent::class) // Available across the whole app
class FormModule {

    // ----------------------------
    // Provides a FormValidator instance
    // Responsible for validating form input fields
    // ----------------------------
    @Provides
    fun provideFormValidator() = FormValidator()

    // ----------------------------
    // Provides a FormConverter instance
    // Responsible for converting form data between different types or models
    // ----------------------------
    @Provides
    fun provideFormConverter() = FormConverter()

    // ----------------------------
    // Provides a FormFormater instance
    // Responsible for formatting form input/output (e.g., dates, numbers)
    // ----------------------------
    @Provides
    fun provideFormFormater() = FormFormater()
}
