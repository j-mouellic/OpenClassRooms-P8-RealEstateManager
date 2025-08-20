package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.ui.form.converter.FormConverter
import com.julien.mouellic.realestatemanager.ui.form.formater.FormFormater
import com.julien.mouellic.realestatemanager.ui.form.validator.FormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FormModule {

    @Provides
    fun provideFormValidator() = FormValidator()

    @Provides
    fun provideFormConverter() = FormConverter()

    @Provides
    fun provideFormFormater() = FormFormater()
}